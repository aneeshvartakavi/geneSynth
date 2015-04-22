package geneSynth;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Import Max related classes
import com.cycling74.max.Atom;
import com.cycling74.max.DataTypes;
import com.cycling74.max.MaxObject;

// The main class for the external
// GA is based on  http://www.theprojectspot.com/tutorial-post/creating-a-genetic-algorithm-for-beginners/3

public class GeneticMorphing extends MaxObject
{
	// 'Target' set of notes to play, a bit string representing the state of 48 notes
	String solutionString = "000000000000100000000000101010110101100000000000";
	// The set of notes to start out with
	String originalPop ="000000000000100000000000000000000000101010010011";
	int generationCount;	
    Population myPop;
    // Need just one note per 'bang' fow now
    int notesPerUpdate = 1;
    // Initialize variable to contain new solution during transitions
    String newSol="000000000000000000000000000000000000101010010011";
    // Set how many milliseconds between notes
    volatile float bangInterval = 500;
    
 
	public GeneticMorphing(Atom[] args)
	{		
		//create the inlets and outlets for Max objects
		declareInlets(new int[]{DataTypes.ALL,DataTypes.ALL});
		declareOutlets(new int[]{DataTypes.ALL});	
		//Initialize genetic related variables
		FitnessCalc.setSolution(solutionString);
		myPop = new Population(100, originalPop);
	}
    	
	// Change the solution of the GA
	public void setSolution(String newSolution) 
	{	
		// Implement logic to change solution here
		
		if(newSolution.equals("red")) 
		{
			newSol = "110010111001100000000000100000000000000000000000";
		}
		
		if(newSolution.equals("blue")) 
		{
			newSol = "000000000000101101011010100000000000000000000000";	
		}
		
		if(newSolution.equals("green")) 
		{
			newSol = "000000000000000000000000101010110101100000000000";	
		}
		if(newSolution.equals("yellow")) 
		{
			newSol = "000000000000000000000000000000000000101011010101";	
		}
		
		// Set new fitness string
		FitnessCalc.setSolution(newSol);
		// Create a new population with previous string
		myPop = new Population(100, solutionString);
		// Replace old solution string with new one
		solutionString = newSol;
		// Reset generation count
		generationCount = 0;
	}
	
	// Can overload the 'setSolution' function to react differently to different data types
	public void setSolution(int a) 
	{
		String ab = Long.toBinaryString(a);
		FitnessCalc.setSolution(ab);
		generationCount = 0;	
	}

	// Function is called when a bang is received at an inlet
	public void bang()
	{
		// Evolution the population by one step until we reach an optimum solution
		if(myPop.getFittest().getFitness() < FitnessCalc.getMaxFitness())
		{
			myPop = Algorithm.evolvePopulation(myPop);
			generationCount++;
			System.out.println("Generation: " + generationCount + " Fittest: " + myPop.getFittest().getFitness());
		}
		else
		{
			// Depending on your input, the result will be constant once the solution has been reached
			System.out.println("Solution found!");
//			System.out.println("Generation: " + generationCount);
//			System.out.println("Genes:");
//			System.out.println(myPop.getFittest());
		}
		
		// Get a list of notes to play
		final List<Integer> notesToPlay = selectNotes(myPop.getFittest().toString(), notesPerUpdate);
		
		// Play notes in a new thread
		new Thread()
		{
			public void run()
			{
//				long startTime = System.currentTimeMillis();
				int notesPlayed = 0;
				while(notesPlayed < notesPerUpdate){
					outlet(0, notesToPlay.get(notesPlayed) + 24);
					notesPlayed++;
					if(notesPlayed < notesPerUpdate)
					{					
						try 
						{
							Thread.sleep((long) (bangInterval / notesPerUpdate));
						} 
						
						catch (InterruptedException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}.start();
	}
    
	// Set the number of milliseconds between notes
	public void inlet(int i)
	{	
		bangInterval = (float)i;
	}
    // Can overload this
	public void inlet(float f)
	{
		bangInterval = f;
	}
	
	// Select and return a list of notes
	private List<Integer> selectNotes(String fittest, int nNotesToPlay)
	{
		List<Integer> possibleNotes = new ArrayList<Integer>();
		for(int i=0; i<fittest.length();i++)
		{
			if(fittest.charAt(i) == '1')
			{
				possibleNotes.add(i);
			}
		}
		
		List<Integer> notesToPlay = new ArrayList<Integer>();
		Random rand = new Random();
		for(int i=0; i< nNotesToPlay; i++)
		{
			if(possibleNotes.size()>0)
			{
				notesToPlay.add(possibleNotes.get(rand.nextInt(possibleNotes.size())));
			}
			else
			{
				notesToPlay.add(1);
			}
		}
		
		return notesToPlay;
	}
}