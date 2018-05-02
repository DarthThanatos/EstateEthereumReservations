pragma solidity ^0.4.0;

contract Reservations {

    address contractOwner;

    struct Estate{
        string estatesOwnerAddressString;
        string name;
        uint price;
        bool[] daysAvailabilityStates;
        bool[] daysReservationStates;
        bool[] paidForDayReservation;
        string[] tenantAddressesStrings;
        uint globalIndex;
    }

    mapping (address => Estate[]) estatesByOwner;

    Estate[] allEstates;

    event PublishedEstate(string estatesOwnerAddressString, string name, uint price, bool[]daysAvailabilityStates);
    event ReservationMade(string estateOwnerAddressString, uint estateIndex, string name, string clientAddrString, uint day);
    event ReservationCanceled(string estateOwnerAddressString, uint estateIndex, string name, string clientAddrString, uint day);

    function Reservations() public {
        contractOwner = msg.sender;
    }

    function publishEstate(string name, uint price, bool[] daysAvailabilityStates, bool[] daysReservationStates){
        address estatesOwner = msg.sender;

        string[] memory tenantAddressesStrings =  new string[](7);
        bool[] memory paidForDayReservation = new bool[](7);
        for(uint i = 0; i < 7; i++) {
            tenantAddressesStrings[i] = "";
            paidForDayReservation[i] = false;
        }

        string memory estatesOwnerAddressString = toString(estatesOwner);
        Estate memory estate = Estate(estatesOwnerAddressString, name, price, daysAvailabilityStates, daysReservationStates, paidForDayReservation, tenantAddressesStrings, allEstates.length);
        estatesByOwner[estatesOwner].push(estate);
        allEstates.push(estate);

        PublishedEstate(estatesOwnerAddressString, name, price, daysAvailabilityStates);
    }


    function tryToChangeAvailableDay(uint estateLocalIndex, uint day, bool available) public{
        Estate estate = estatesByOwner[msg.sender][estateLocalIndex];
        if(estate.daysReservationStates[day]) return;

        estate.daysAvailabilityStates[day] = available;
        allEstates[estate.globalIndex].daysAvailabilityStates[day] = available;
    }

    function getEstateOfOwnerByIndex(address estatesOwner, uint index) constant public returns(string, string , uint , bool[] , bool[] ){
        Estate estate = estatesByOwner[estatesOwner][index];
        return (estate.estatesOwnerAddressString, estate.name, estate.price, estate.daysAvailabilityStates, estate.daysReservationStates);
    }

    function getEstateByIndex(uint index) constant public returns(string, string , uint , bool[] , bool[] ){
        Estate estate = allEstates[index];
        return (estate.estatesOwnerAddressString, estate.name, estate.price, estate.daysAvailabilityStates, estate.daysReservationStates);
    }

    function getOwnerEstatesAmount(address estatesOwner)constant public returns(uint){
        return estatesByOwner[estatesOwner].length;
    }

    function getAllEstatesAmount() constant public returns(uint){
        return allEstates.length;
    }


    function tryToReserve(address estateOwner, uint estateIndex, uint day) public{
        address client = msg.sender;
        Estate estate = estatesByOwner[estateOwner][estateIndex];
        if(estateOwner == client) return;
        if(day < 0 && day >= 7) return;
        if(!estate.daysAvailabilityStates[day] || estate.daysReservationStates[day]) return;

        estate.daysReservationStates[day] = true;
        estate.tenantAddressesStrings[day] = toString(client);

        allEstates[estate.globalIndex].daysReservationStates[day] = true;
        allEstates[estate.globalIndex].tenantAddressesStrings[day] = toString(client);

        ReservationMade(
            toString(estateOwner),
            estateIndex,
            estatesByOwner[estateOwner][estateIndex].name,
            estatesByOwner[estateOwner][estateIndex].tenantAddressesStrings[day],
            day
        );
    }

    function getTenantOfOwner(address estateOwner, uint estateIndex, uint day) constant public returns(string){
        return estatesByOwner[estateOwner][estateIndex].tenantAddressesStrings[day];
    }

    function getTenant(uint estateIndex, uint day) constant public returns(string){
        return allEstates[estateIndex].tenantAddressesStrings[day];
    }

    function tryToCancel(address estateOwner, uint estateIndex, uint day) public{
        address client = msg.sender;
        if(estateOwner == client) return;

        Estate estate = estatesByOwner[estateOwner][estateIndex];

        if(day < 0 && day >= 7) return;
        if(!estate.daysAvailabilityStates[day] || !estate.daysReservationStates[day]) return;

        string memory clientStringAddr = toString(client);
        string memory currentTenant = estate.tenantAddressesStrings[day];
        if(!stringsEqual(currentTenant, clientStringAddr)) return;

        estate.daysReservationStates[day] = false;
        estate.tenantAddressesStrings[day] = "";

        allEstates[estate.globalIndex].daysReservationStates[day] = false;
        allEstates[estate.globalIndex].tenantAddressesStrings[day] = "";

        ReservationCanceled(
            toString(estateOwner),
            estateIndex,
            estatesByOwner[estateOwner][estateIndex].name,
            clientStringAddr,
            day
        );
    }

    function stringsEqual (string a, string b) returns (bool){
        return keccak256(a) == keccak256(b);
    }

    function toString(address x) returns (string) {
        bytes memory s = new bytes(40);
        for (uint i = 0; i < 20; i++) {
            byte b = byte(uint8(uint(x) / (2**(8*(19 - i)))));
            byte hi = byte(uint8(b) / 16);
            byte lo = byte(uint8(b) - 16 * uint8(hi));
            s[2*i] = char(hi);
            s[2*i+1] = char(lo);
        }
        return string(s);
    }

    function char(byte b) returns (byte c) {
        if (b < 10) return byte(uint8(b) + 0x30);
        else return byte(uint8(b) + 0x57);
    }

}