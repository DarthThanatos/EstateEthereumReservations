pragma solidity ^0.4.0;

contract Reservations {

    address contractOwner;

    struct Estate{
        string estatesOwnerAddressString;
        string name;
        uint price;
        bool[] daysAvailabilityStates;
        bool[] daysReservationStates;
        string[] tenantAddressesStrings;
    }

    mapping (address => Estate[]) estatesByOwner;
    Estate[] allEstates;

    event PublishedEstate(string estatesOwnerAddressString, string name, uint price, bool[]daysAvailabilityStates); //

    function Reservations() public {
        contractOwner = msg.sender;
    }

    function publishEstate(string name, uint price, bool[] daysAvailabilityStates, bool[] daysReservationStates){
        address estatesOwner = msg.sender;
        string[] memory tenantAddressesStrings =  new string[](7);
        string memory estatesOwnerAddressString = toString(estatesOwner);
        Estate memory estate = Estate(estatesOwnerAddressString, name, price, daysAvailabilityStates, daysReservationStates, tenantAddressesStrings);
        estatesByOwner[estatesOwner].push(estate);
        allEstates.push(estate);

        PublishedEstate(estatesOwnerAddressString, name, price, daysAvailabilityStates); //
    }

    function getEstateOfOwnerByIndex(address estatesOwner, uint index) constant public returns(string, string , uint , bool[] , bool[] ){
        Estate memory estate = estatesByOwner[estatesOwner][index];
        return (estate.estatesOwnerAddressString, estate.name, estate.price, estate.daysAvailabilityStates, estate.daysReservationStates);
    }

    function getEstateByIndex(uint index) constant public returns(string, string , uint , bool[] , bool[] ){
        Estate memory estate = allEstates[index];
        return (estate.estatesOwnerAddressString, estate.name, estate.price, estate.daysAvailabilityStates, estate.daysReservationStates);
    }

    function getOwnerEstatesAmount(address estatesOwner)constant public returns(uint){
        return estatesByOwner[estatesOwner].length;
    }

    function getAllEstatesAmount() constant public returns(uint){
        return allEstates.length;
    }

    function tryToReserve(address client, uint day) public{

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