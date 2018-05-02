pragma solidity ^0.4.0;

contract Reservations{
    function payForDay(address estateOwner, uint estateLocalIndex, uint day, uint amount) public {}
    function getRemaningQuote(address estateOwner, uint estateLocalIndex, uint day) public returns(uint){}
    function isDayReserved(address estateOwner, uint estateLocalIndex, uint day) public returns(bool){}
}

contract Coin {

    // The keyword "public" makes those variables
    // readable from outside.
    address minter;
    mapping (address => uint) public balances;

    // Events allow light clients to react on
    // changes efficiently.
    event Sent(string from, string to, uint amount, uint pastFromBalance, uint currentFromBalance, uint pastToBalance, uint currentToBalance);
    event Minted(string receiver, uint amount);

    // This is the constructor whose code is
    // run only when the contract is created.
    function Coin() public {
        minter = msg.sender;
    }

    function mint(address receiver, uint amount) public {
        if (msg.sender != minter) return;
        balances[receiver] += amount;
        Minted(toString(receiver), amount);
    }

    function getMinter() constant public returns(address){
        return minter;
    }

    function getBalance(address receiver) public returns(uint){
        return balances[receiver];
    }

    function send(address receiver, uint amount) public {
        if (balances[msg.sender] < amount) return;
        uint prev_src_balance = balances[msg.sender];
        uint prev_target_balance = balances[receiver];
        balances[msg.sender] -= amount;
        balances[receiver] += amount;
        uint cur_src_balnce = balances[msg.sender];
        uint cur_target_balance = balances[receiver];
        Sent(toString(msg.sender), toString(receiver), amount, prev_src_balance, cur_src_balnce, prev_target_balance, cur_target_balance);
    }

    function payForReservation(address estateOwner, uint estateLocalIndex, uint amount, uint day, address reservationsAddr){
        Reservations reservations = Reservations(reservationsAddr);
        uint amountLeft =  reservations.getRemaningQuote(estateOwner, estateLocalIndex, day);
        uint amountToSend = min(amountLeft, amount);
        if(!reservations.isDayReserved(estateOwner, estateLocalIndex, day)) return;
        if (balances[msg.sender] < amountToSend) return;
        send(estateOwner, amountToSend);
        reservations.payForDay(estateOwner, estateLocalIndex, day, amountToSend);
    }

    function min(uint a, uint b) private returns (uint) {
        return a < b ? a : b;
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
