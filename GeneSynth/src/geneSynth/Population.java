package geneSynth;

public class Population 
{

    Individual[] individuals;
    private boolean usePredefined = false;

    /*
     * Constructors
     */
    // Create a population
    public Population(String[] originalPop)
    {
    	individuals = new Individual[originalPop.length];
		for (int i = 0; i < originalPop.length; i++) 
		{
			Individual newIndividual = new Individual(originalPop[i]);
			newIndividual.generateIndividual();
			saveIndividual(i, newIndividual);
		}
    }
    
    public Population(int populationSize, boolean initialize) 
    {
    	individuals = new Individual[populationSize];
    	// Initialize population
    	if (initialize) 
    	{
    		if(usePredefined)
    		{
    			Individual indiv1 = new Individual("101010101010");
    			Individual indiv2 = new Individual("010010101101");
    			saveIndividual(0, indiv1);
    			saveIndividual(1, indiv2);
    			
    		}
    		else
    		{
    			// Loop and create individuals
    			for (int i = 0; i < size(); i++) 
    			{
    				Individual newIndividual = new Individual();
    				newIndividual.generateIndividual();
    				saveIndividual(i, newIndividual);
    			}
    		}
    	}
    }
    
    public Population(int populationSize, String constraintString)
    {
    	individuals = new Individual[populationSize];
		for (int i = 0; i < size(); i++) 
		{
			Individual newIndividual = new Individual();
			newIndividual.generateIndividual(constraintString);
			saveIndividual(i, newIndividual);
		}
    }



    /* Getters */
    public Individual getIndividual(int index) 
    {
        return individuals[index];
    }

    public Individual getFittest() 
    {
        Individual fittest = individuals[0];
        // Loop through individuals to find fittest
        for (int i = 0; i < size(); i++) 
        {
            if (fittest.getFitness() <= getIndividual(i).getFitness()) 
            {
                fittest = getIndividual(i);
            }
        }
        return fittest;
    }

    /* Public methods */
    // Get population size
    public int size() 
    {
        return individuals.length;
    }

    // Save individual
    public void saveIndividual(int index, Individual indiv) 
    {
        individuals[index] = indiv;
    }
}