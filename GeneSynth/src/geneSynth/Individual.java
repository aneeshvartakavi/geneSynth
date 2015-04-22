package geneSynth;

public class Individual {

    static int defaultGeneLength = 84;
    private byte[] genes = new byte[defaultGeneLength];
    // Cache
    private int fitness = 0;
    
    
    public Individual(){
    	
    }
    public Individual(String newGenes)
    {
        genes = new byte[newGenes.length()];
        // Loop through each character of our string and save it in our byte 
        // array
        for (int i = 0; i < newGenes.length(); i++) 
        {
            String character = newGenes.substring(i, i + 1);
            if (character.contains("0") || character.contains("1")) 
            {
            	genes[i] = Byte.parseByte(character);
            } 
            else 
            {
            	genes[i] = (byte)0;
            }
        }
    }

    // Create a random individual
    public void generateIndividual() 
    {
        for (int i = 0; i < size(); i++) 
        {
            byte gene = (byte) Math.round(Math.random());
            genes[i] = gene;
        }
    }
    
    public void generateIndividual(String constraintString)
    {
    	for(int i=0;i<constraintString.length();i++)
    	{
    		if(constraintString.charAt(i) == ('1'))
    		{
    			if(Math.random() > 0.2)
    			{
    				byte gene = (byte) 1;
    				genes[i] = gene;
    			}
    			else
    			{
    				byte gene = (byte) 0;
    				genes[i] = gene;
    			}
    		}
    	}
    }

    /* Getters and setters */
    // Use this if you want to create individuals with different gene lengths
    public static void setDefaultGeneLength(int length) 
    {
        defaultGeneLength = length;
    }
    
    public byte getGene(int index) 
    {
        return genes[index];
    }

    public void setGene(int index, byte value) 
    {
        genes[index] = value;
        fitness = 0;
    }

    /* Public methods */
    public int size() 
    {
        return genes.length;
    }

    public int getFitness() 
    {
        if (fitness == 0) 
        {
            fitness = FitnessCalc.getFitness(this);
        }
        return fitness;
    }

    @Override
    public String toString() 
    {
        String geneString = "";
        for (int i = 0; i < size(); i++) 
        {
            geneString += getGene(i);
        }
        return geneString;
    }
}