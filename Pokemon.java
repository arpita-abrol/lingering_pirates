/*
*/

import java.util.*;
import cs1.Keyboard;
import java.io.*;


public class Pokemon {

    //instance vars
    private String _name;     //pokemon name
    private String _type;     //pokemon type
    private int _level;       //level of pokemon
    private int _num;         //pokedex number
    private int[] _attack = new int[2];      //pokemon attack--determines attack strength
    private int[] _defense = new int[2];     //pokemon defense--determines damage taken
    private int[] _HP = new int[2];          //pokemon HP--determines max damage
    private int[] _speed = new int[2];       //pokemon speed--determines first attacker
    private int[] _exp = new int[2];        //pokemon exp--determines current exp/needed exp
    private int _numMoves = 0;    //number of moves pokemon has (max 4)
    private String[][]  _moves = new String[4][2];  //pokemon moves [[m1,p1][m2,p2][m3,p3][m4,p4]]
    private boolean isCaught;


    //Constructors
    public Pokemon( String name ) {
	int num = CSVMaster.searchCSV( name, CSVMaster.pokeStats );
	String[] data = CSVMaster.singleLine( CSVMaster.pokeStats.get(num) );
	_name = name;
	_type = data[6];
	_level = 1;
	_num = num;
        setAttack(Integer.parseInt(data[3]));
        setDefense(Integer.parseInt(data[4]));
        setHP(Integer.parseInt(data[2]));
        setSpeed(Integer.parseInt(data[5]));
	setExp();
	setRandomMoves();
	isCaught = false;
    }
    
    public Pokemon( String name, int level ) {
	this( name );
	_level = level;
	setAttack( getAttack() + (int)(Math.random()*2.5*level) );
	setDefense( getDefense() + (int)(Math.random()*2*level) );
	setHP( getHP() + (int)(Math.random()*4*level) );
	setSpeed( getSpeed() + (int)(Math.random()*2*level) );
	setExp();
	removeAllMoves();
	setRandomMoves();
    }

    public Pokemon( String name, int level, String[] moveList ) {
	this( name, level );
	removeAllMoves();
	setGivenMoves(moveList);
    }

    

    //accessors
    public String getName() {
	return _name;
    }  

    public String getType() {
	return _type;
    }
    
    public int getLevel() {
	return _level;
    }

    public int getNum() {
	return _num;
    }

    public int getAttackT() {
	return _attack[0];
    }

    public int getAttack() {
	return _attack[1];
    }

    public int getDefenseT() {
	return _defense[0];
    }

    public int getDefense() {
	return _defense[1];
    }

    public int getHPT() {
	return _HP[0];
    }

    public int getHP() {
	return _HP[1];
    }

    public int getSpeedT() {
	return _speed[0];
    }

    public int getSpeed() {
	return _speed[1];
    }

    public int getExpT() {
	return _exp[0];
    }

    public int getExp() {
	return _exp[1];
    }

    public int getNumMoves() {
	return _numMoves;
    }

    public String getMove( int move ) {
	return "" + _moves[move][0];
    }

    public String getPower( int move ) {
	return _moves[move][1];
    }

    public boolean getIsCaught() {
	return isCaught;
    }


    
    //mutators
    public void setName( String newName ) {
	_name = newName;
    }
    
    public void setLevel( int newLevel ) {
        _level = newLevel;
    }

    public void setNum( int newNum ) {
	_num = newNum;
    }

    public void setAttackT( int newAttack ) {
	_attack[0] = newAttack;
    }

    public void setAttack( int newAttack ) {
	if( newAttack > 100 ) { newAttack = 100; }
	_attack[0] = newAttack;
	_attack[1] = newAttack;
    }

    public void setDefenseT( int newDefense ) {
	_defense[0] = newDefense;
    }

    public void setDefense( int newDefense ) {
	_defense[0] = newDefense;
	_defense[1] = newDefense;
    }

    public void setHPT( int newHP ) {
	_HP[0] = newHP;
    }

    public void setHP( int newHP ) {
	_HP[0] = newHP;
	_HP[1] = newHP;
    }

    public void setSpeedT( int newSpeed ) {
	_speed[0] = newSpeed;
    }

    public void setSpeed( int newSpeed ) {
	_speed[0] = newSpeed;
	_speed[1] = newSpeed;
    }
    
    public void setExpT( int newExp ) {
	_exp[0] = newExp;
	if( getExpT() >= getExp() ) {
	    levelUp();
	}
	if( getLevel() == 100 ) {
	    _exp[0] = 0;
	}
    }
		   
    public void setExp() {
	_exp[0] = 0;
	_exp[1] = (int)( getLevel()*getLevel()*getLevel()+1 / 2); //reduced for the sake of the game
	if( getLevel() == 100 ) {
	    _exp[1] = 999999999;
	}
    }

    public void setIsCaught( Boolean bool ) {
	isCaught = bool;
    }

    public void setNumMoves( int num ) {
	_numMoves = num;
	if( _numMoves > 4 ) { _numMoves = 4; }
    }

    //assigns a pokemon a move
    public void setMove( int move, String m1 ) {
	_moves[move][0] = m1;
	_moves[move][1] = "" + findPower(m1);
    }

    //removes a move
    public void removeMove( int move ) {
	_moves[move][0] = null;
	_moves[move][1] = null;
    }

    //removes all moves
    public void removeAllMoves() {
	setNumMoves(0);
	for( int x = 0; x < 4; x++ ) {
	    removeMove(x);
	}
    }

    //gives pokemon moves in array
    public void setGivenMoves( String[] moves ) {
	removeAllMoves();
	for( int x = 0; x < moves.length; x++ ) {
	    setMove( x, moves[x] );
	    setNumMoves( getNumMoves() + 1 );
	}
    }

    //randomly chooses 4 moves out of all possible moves
    public void setRandomMoves() {
	ArrayList<String> possibleMoves = new ArrayList<String>();
        String m = CSVMaster.singleLine( CSVMaster.pokeMoves.get(getNum()))[2];
	String[] born = m.split("1");
	for( int x = 0; x < born.length; x++ ) {
	    possibleMoves.add(born[x]);
	    //System.out.println(born[x]);
	}
	for( int x = 1; x <= getLevel(); x++ ) {
	    String move = CSVMaster.singleLine( CSVMaster.pokeMoves.get(getNum()))[x+2] ;
	    if( move.length() > 1 ) {
		possibleMoves.add(move);
	    }
	}
	//System.out.println(possibleMoves.size());
	//give pokemon moves
	if( possibleMoves.size() <= 4 ) {
	    for( int x = 0; x <= possibleMoves.size()-1; x++ ) {
		setMove( x, possibleMoves.get(x) );
		setNumMoves( getNumMoves() + 1 );
	    }
	}
	else {
	    ArrayList<Integer> current = new ArrayList<Integer>();
	    for( int x = 0; x < 4; ) {
		int y = (int)(Math.random() * possibleMoves.size()-1);
		boolean contain = false;
		for ( int z = 0; z < current.size(); z++ ) {
		    if( current.get(z).equals(y) ) {
			contain = true;
		    }
		}
		if(!contain && !(possibleMoves.get(y).equals(null))) {
		    //System.out.println( x + possibleMoves.get(y) );
		    setMove( x, possibleMoves.get(y) );
		    setNumMoves( getNumMoves() + 1 );
		    x += 1;
		    //System.out.println( getAllMoves());
		    current.add(y);
		}
	    }
	}
    }

    //NOTE: NEED TO SANITIZE 
    //adds move to pokemon if leveling up
    public void addMove() {
	//System.out.println("L");
	String move = CSVMaster.singleLine( CSVMaster.pokeMoves.get(getNum()))[getLevel()+1];
	//System.out.println(move);
	if( !(move.length() > 1) ) {
	    //System.out.println("J");
	    return;
	}
	if( getNumMoves() < 4 ) {
	    setMove( getNumMoves()+1, move );
	    System.out.println( getName() + " has learned " + move + "!" );
	}
	else {
	    System.out.println( getName() + " can learn " + move + ". However, " + getName() + " already knows 4 moves. Delete a move? [y/n]" );
	    String resp = Keyboard.readString();
	    if( !(resp.toLowerCase().equals("y") || resp.toLowerCase().equals("yes") || resp.toLowerCase().equals("n") || resp.toLowerCase().equals("no") ) ) {
		System.out.println("y/n");
		resp = Keyboard.readString();
	    }
	    if( resp.toLowerCase().equals("y") || resp.toLowerCase().equals("yes") ) {
		System.out.println( getAllMoves() );
		System.out.println( "Select a move." );
		resp = Keyboard.readString();
		if( "12345".indexOf(resp)==-1 ) {
		    System.out.println( "Select the number to the left of a move to replace it. Or \"n\" to keep all moves the same.");
		}
		if( resp.toLowerCase().equals("n") || resp.toLowerCase().equals("no") ) {
		    return;
		}
		setMove( Integer.parseInt(resp), move );
		System.out.println( getName() + " has learned " + move + "!" );
	    }
	    else if( resp.toLowerCase().equals("n") || resp.toLowerCase().equals("no") ) {
		return;
	    }
	}
    }


    
    //other methods

    //levels up pokemon 
    public void levelUp() {
	if( getLevel() < 100 ) {
	    _level += 1;
	    setAttack( getAttack() + (int)(Math.random() * 2.5) );
	    setDefense( getDefense() + (int)(Math.random() * 2) );
	    setHP( getHP() + (int)(Math.random() * 3) );
	    setSpeed( getSpeed() + (int)(Math.random() * 2) );
	    setExp();
	    System.out.println( _name + " is now at level " + _level + "!");
	    addMove();
	    evolve();
	}
    }

    //NOTE: NEED TO ASK IF YOU WANT TO EVOLVE AND DO STUFF FOR SPECIAL CASES
    //evolves pokemon if possible
    public void evolve() {
	String[] data = CSVMaster.singleLine( CSVMaster.pokeEvolutions.get(getNum()) );
	if( data[2].equals("true") ) {
	   
	    //evolve
	    if( data[3].equals("-1") ) {
		System.out.println( getName() + " is evolving! Allow pokemon to evolve? [y/n]" );
		String ans = Keyboard.readString();
		if( ans.toLowerCase().equals("y") || ans.toLowerCase().equals("yes") ) {
		    //stones
		}
	    }
	    if( data[3].equals("-2") ) {
		System.out.println( getName() + " is evolving! Allow pokemon to evolve? [y/n]" );
		String ans = Keyboard.readString();
		if( ans.toLowerCase().equals("y") || ans.toLowerCase().equals("yes") ) {
		    //trade
		}
	    }
	    if( data[3].equals("-3") ) {
		System.out.println( getName() + " is evolving! Allow pokemon to evolve? [y/n]" );
		String ans = Keyboard.readString();
		if( ans.toLowerCase().equals("y") || ans.toLowerCase().equals("yes") ) {
		    //EEVEE
		}
	    }
	    if( Integer.parseInt(data[3]) <= getLevel() ) {
		System.out.println( getName() + " is evolving! Allow pokemon to evolve? [y/n]" );
		String ans = Keyboard.readString();
		if( ans.toLowerCase().equals("y") || ans.toLowerCase().equals("yes") ) {
		    setName( data[4] );
		    setNum( getNum() + 1 );
		    setAttack( getAttack() + 5 );
		    setDefense( getDefense() + 3 );
		    setHP( getHP() + 5 );
		    setSpeed( getSpeed() + 2 );
		    System.out.println(data[1] + " has evolved to " + getName() + "!");
		}
	    }
	}
    }

    //returns if pokemon is alive
    public boolean isAlive() {
	return( getHPT() > 0 );
    }

    //finds the power of a selected move
    public int findPower( String move ) {
	System.out.println( move );
	int num = CSVMaster.searchCSV( move, CSVMaster.moves, 0 );
	return Integer.parseInt(CSVMaster.singleLine( CSVMaster.moves.get(num) )[2]);
    }

    //NOTE: NOT SANITIZED
    //trainer selects move
    public int selectMove() {
	System.out.println("Which move would you like to use?");
	System.out.println( getAllMoves() );
	int move = Keyboard.readInt();
	return move;
    }

    //NOTE: DOESN'T ACCOUNT FOR TYPES
    //calculates damage
    public int calcDamage( String power, Pokemon opp ) {
	int damage = this.getAttack() * Integer.parseInt(power) / (opp.getDefense() + opp.getLevel() );
	if( Integer.parseInt(power) <= 0 ) {
	    damage = 0;
	}
	return damage;
    }
    
    //attack another pokemon
    public void attack( Pokemon opp ) {
	int move = this.selectMove();
	int damage = this.calcDamage(_moves[move][1],opp);
	opp.setHPT( opp.getHPT() - damage );
	System.out.println(this.getName() + " used " + _moves[move][0] + "!");
	System.out.println(opp.getName() + " took " + damage + " damage!\n\n");
    }

    //trainer's pokemon gets attacked
    public void attackT( Pokemon opp ) {
	int move = (int)(this.getNumMoves() * Math.random());
	int damage= (int)(this.calcDamage(_moves[move][1],opp)*.5);
	opp.setHPT( opp.getHPT() - damage );
	if (opp.getHPT()<0){
	    opp.setHPT(0);
	}
	System.out.println(this.getName() + " used " + _moves[move][0] + "!");
	System.out.println(opp.getName() + " took " + damage + " damage!");
    }

    //battles another pokemon
    public void battle( Pokemon opp, Trainer person ) {

	    if( this.getSpeed() >= opp.getSpeed() ) {
		this.attack(opp);
		if( opp.isAlive() ) {
		    opp.attackT(this);
		}
	    }else{
		opp.attackT(this);
		if( this.isAlive() ) {
		    this.attack(opp);
		}
	    }
	
	if( !opp.isAlive() ) {
	    int newExp = (opp.getLevel() * (int)(Math.random() * 3 + 1));
	    this.setExpT( this.getExpT() + newExp );
	    System.out.println( this.getName() + " won. " + this.getName() + " gained " + newExp + " exp." );
	}
	else if(!this.isAlive()) {
	    System.out.println( opp.getName() + " won." );
	}
    }

        //battles another pokemon
    public void battleTrainer( Pokemon opp, Trainer person ) {
	while( this.isAlive() && opp.isAlive() ) {
	    System.out.println( this + "\n" + opp );
	    System.out.println("Would you like to (1)battle or (2)use a potion");
	    String rep = Keyboard.readString();
	    if( rep.equals("1") ) {
		if( this.getSpeed() >= opp.getSpeed() ) {
		    this.attack(opp);
		    if( opp.isAlive() ) {
			opp.attackT(this);
		    }
		}
		else {
		    opp.attackT(this);
		    if( this.isAlive() ) {
			this.attack(opp);
		    }
		}
	    }
	    else if( rep.equals("2") ) {
		person.usePotions(this);
		opp.attack(this);
	    }
	}
	if( this.isAlive() ) {
	    int newExp = (opp.getLevel() * (int)(Math.random() * 3 + 1));
	    this.setExpT( this.getExpT() + newExp );
	    System.out.println( this.getName() + " won. " + this.getName() + " gained " + newExp + " exp." );
	}
	else {
	    System.out.println( opp.getName() + " won." );
	}
    }

    public String getAllMoves() {
	String fin = "";
	for( int x = 0; x < getNumMoves(); x++ ) {
	    fin += x + ": " + getMove(x) + "\t" + getPower(x) + "\n";
	}
	return fin;
    }

    public String toString() {
	String fin = _name;
	fin += "\tLevel: " + getLevel() + "\tAttack: " + getAttackT() + "\tDefense: " + getDefenseT() + "\tHP: " + getHPT();
	return fin + "\n";
    }




    public static String getRandom( int town ) {
	ArrayList<String> arr = new ArrayList<String>();
	arr = CSVMaster.CSVtoArray("Town" + town + ".csv");
	int pNum = (int)(Math.random() * arr.size()-1);
	double random = Math.random()*100;
	if( 100-Double.parseDouble(CSVMaster.singleLine(arr.get(pNum+1))[2]) < random ) {
	    String pokeName = CSVMaster.singleLine(arr.get(pNum+1))[1];
	    System.out.println( Double.parseDouble(CSVMaster.singleLine(arr.get(pNum+1))[2]) + "\t" + random );
	    return pokeName;
	}
	else{
	    return getRandom( town );
	}
    }



    
    //for testing purposes only
    public static void main( String[]args ) {
	String[] pokeMoves = {"Tackle","Poison Powder"};
	//Pokemon sample = new Pokemon("Blastoise",4);
	Pokemon sample2 = new Pokemon("Squirtle",44);
	sample2.setExpT(1111111);
	//Trainer sample3 = new Trainer("H");
	//System.out.println(sample + "\n" + sample.getAllMoves());
	//System.out.println(sample2 + "\n" + sample2.getAllMoves());
	//sample2.battle(sample,sample3);
	//System.out.println( sample.getNumMoves() );
	//System.out.println( getRandom(0) );
	System.out.println( sample2 + "\n" + sample2.getAllMoves() + "\n" + sample2.getMove(0));
    }
	    

}
