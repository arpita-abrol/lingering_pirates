import cs1.Keyboard;

public class PokemonDriver {
    public static void main(String[] args){
    System.out.println("Please enter your name");
    String TrainerName= Keyboard.readString();
    System.out.println("Welcome Trainer "+ TrainerName+"!");
    
    Trainer test= new Trainer(TrainerName);
    test.chooseStarter();
    
    }
}
