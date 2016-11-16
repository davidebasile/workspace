package CA;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

import FSA.FSA;
import FSA.Simulator;
import FSA.Transition;


/** 
 * Class implementing a Contract Automaton and its functionalities
 * @author Davide Basile
 *
 */
@SuppressWarnings("serial")
public class CA  extends FSA implements java.io.Serializable
{
	private int rank;
	private int[] initial;
	private int[] states;
	private int[][] finalstates; 
	//private CATransition[] tra;
	private static String message = "*** CA ***\n The alphabet is represented by integers: " +
			" negative numbers are request actions, positive are offer actions, 0 stands for idle\n";
	
	/**
	 * Invoke the super constructor and take in input the added new parameters of the automaton
	 */
	public CA()
	{
		super(message);
		try{
			System.out.println();
	        this.rank = 1;
	        this.states = new int[1];
	        this.states[0] = super.getStates();
	        this.initial = new int[1];
	        initial[0] = super.getInitial();
	        finalstates = new int[1][super.getFinalStates().length];
	        finalstates[0]= super.getFinalStates();
	       // this.tra=(CATransition[])super.getTransition();
	        //super.write(this);
	        this.printToFile();
		}
		catch (Exception e){System.out.println("Errore inserimento");}
	}
	
	public CA(int rank, int[] initial, int[] states, int[][] finalstates,CATransition[] tra)
	{
		super(tra);
		this.rank=rank;
		this.initial=initial;
		this.states=states;
		this.finalstates=finalstates;
	}
	
	/**
	 * Print in output a description of the automaton
	 */
	public void print()
	{
		/**
		 * Print in output a description of the automaton
		 */
		System.out.println("Rank: "+this.rank);
		System.out.println("Number of states: "+Arrays.toString(this.getStatesCA()));
		System.out.println("Initial state: " +Arrays.toString(this.getInitialCA()));
		System.out.print("Final states: [");
		for (int i=0;i<finalstates.length;i++)
			System.out.print(Arrays.toString(finalstates[i]));
		System.out.print("]\n");
		System.out.println("Transitions: \n");
		Transition[] t = this.getTransition();
		if (t!=null)
		{
			for (int i=0;i<t.length;i++)
				System.out.println(t[i].toString());
		}
	}
	
	/**
	 * print the description of the CA to a file
	 */
	public void printToFile()
	{
		String name=null;
		InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader myInput = new BufferedReader(reader);
		try {
			System.out.println("Do you want to save this automaton? (write yes or no)");
			if (myInput.readLine().equals("yes"))
			{	
				System.out.println("Write the name of this automaton");
				name= myInput.readLine();
			}
			else return;
			 PrintWriter pr = new PrintWriter(name+".data"); 
			 pr.println("Rank: "+this.rank);
			 pr.println("Number of states: "+Arrays.toString(this.getStatesCA()));
			 pr.println("Initial state: " +Arrays.toString(this.getInitialCA()));
			 pr.print("Final states: [");
			 for (int i=0;i<finalstates.length;i++)
				 pr.print(Arrays.toString(finalstates[i]));
			 pr.print("]\n");
			 pr.println("Transitions: \n");
			 Transition[] t = this.getTransition();
			 if (t!=null)
			 {
				 for (int i=0;i<t.length;i++)
					 pr.println(t[i].toString());
			 }
			 pr.close();
		}catch(Exception e){e.printStackTrace();}
	}
	
	/**
	 * load a CA described in a text file
	 * @param the name of the file
	 * @return	the CA loaded
	 */
	public static CA load(String fileName)
	{
		try
		{
			// Open the file
			FileInputStream fstream = new FileInputStream(fileName+".data");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			int rank=0;
			int[] initial = new int[1];
			int[] states = new int[1];
			int[][] fin = new int[1][];
			CATransition[] t = new CATransition[1];
			CATransition[] fintr = new CATransition[1];
			int pointert=0;
			
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   
			{
				  // Print the content on the console
				  if (strLine.length()>0)
				  {
					  switch(strLine.substring(0,1))
					  {
						  case "R":
						  {
							  Scanner s = new Scanner(strLine);
							  s.useDelimiter("");
							  while (s.hasNext())
							  {
								  if (s.hasNextInt())
								  {
									  rank = s.nextInt();
								  }
								  else
								  {
									  s.next();
								  }
							  }
							  initial = new int[rank];
							  states = new int[rank];
							  fin = new int[rank][];
							  s.close();
							  break;
						  }
						  case "N":
						  {
							  Scanner s = new Scanner(strLine);
							  s.useDelimiter("");
							  int i=0;
							  int lengthT=1;
							  while (s.hasNext())
							  {
								  if (s.hasNextInt())
								  {
									  states[i] = s.nextInt();
									  lengthT*=states[i];
									  i++;
								  }
								  else
									  s.next();
							  }
							  t = new CATransition[lengthT*lengthT*4];//guessed upper bound WARNING
							  s.close();
							  break;
						  }
						  case "I":
						  {
							  Scanner s = new Scanner(strLine);
							  s.useDelimiter("");
							  int i=0;
							  while (s.hasNext())
							  {
								  if (s.hasNextInt())
								  {
									  initial[i] = s.nextInt();
									  i++;
								  }
								  else
									  s.next();
							  }
							  s.close();
							  break;
						  }
						  case "F":
						  {
							  String[] ss=strLine.split("]");
							  for (int ind=0;ind<ss.length;ind++)
							  {
								  Scanner s = new Scanner(ss[ind]);
								  s.useDelimiter("");
								  int i=0;
								  int[] tf = new int[states[i]]; //upper bound
								  while (s.hasNext())
								  {
									  if (s.hasNextInt())
									  {
										  tf[i] = s.nextInt();
										  i++;
									  }
									  else
										  s.next();
								  }
								  fin[ind]= new int[i];
								  for (int ii=0;ii<i;ii++)
									  fin[ind][ii]=tf[ii];
								  s.close();
							  }
							  break;
						  }
						  case "(":
						  {
							  String[] ss=strLine.split("]");
							  int what=0;
							  int[][] store=new int[2][];
							  for (int i=0;i<ss.length;i++)
							  {
								  int[] arr = new int[rank];
								  Scanner s = new Scanner(ss[i]);
								  s.useDelimiter(",|\\[| ");
								  int j=0;
								  while (s.hasNext())
								  {
									  if (s.hasNextInt())
									  {
										 arr[j]=s.nextInt();
										 j++;
									  }
									  else {
										   s.next();
									  }
								  }
								  s.close();
								  if (what==2)
								  {
									  t[pointert]=new CATransition(store[0],store[1],arr);
									  what=0;
									  pointert++;
								  }
								  else
									  store[what]=arr;
								  what++;
							  }						 
							  break;
						  }
					  }
				  }
			}
			br.close();	
			fintr = new CATransition[pointert];
			  for (int i=0;i<pointert;i++)
			  {
				  fintr[i]=t[i];
			  }
			return new CA(rank,initial,states,fin,fintr);
		} catch (Exception e) {e.printStackTrace();}
		return null;
	}
	
	
	/**
	 * Create an instance of the simulator for an FMA
	 */
	protected Simulator createSim()
	{
		//return new FMASimulator(this);
		return null;
	}
	
	/**
	 * 
	 * @param i		the index of the transition to be showed as a message to the user
	 * @return		a new Transition for this automaton
	 */
	protected Transition createTransition(int i)
	{
		return new CATransition(i);
	}
	
	
	/**
	 * 
	 * @return	the array of final states
	 */
	public int[][] getFinalStatesCA()
	{
		return finalstates;
	}
	
	/**
	 * 
	 * @return	the array of states
	 */
	public int[] getStatesCA()
	{
		return states;
	}
	
	/**
	 * 
	 * @return	the array of initial states
	 */
	public int[] getInitialCA()
	{
		return initial;
	}
	
	/**
	 * 
	 * @return the rank of the Contract Automaton
	 */
	public int getRank()
	{
		return rank;
	}
	
	/**
	 * 
	 * @return	the array of transitions
	 */
	public CATransition[] getTransition()
	{
		Transition[] temp = super.getTransition();
		CATransition[] t = new CATransition[temp.length];
		for (int i=0;i<temp.length;i++)
				t[i]=(CATransition)temp[i];
		return t;
	}
	
	/**
	 * The sum all states of all principals
	 * @return The sum all states of all principals
	 */
	public int sumStates()
	{
		int numstates=0;
		for (int i=0;i<states.length;i++)
		{
			numstates+=states[i];
		}
		return numstates;
	}
	
	/**
	 * The product of the states of all principals
	 * @return The product of the states of all principals
	 */
	public int prodStates()
	{
		int prodstates=1;
		for (int i=0;i<states.length;i++)
		{
			prodstates*=states[i];
		}
		return prodstates;
	}
	
	/**
	 * @return a new object CA clone
	 */
	public CA clone()
	{
		CATransition[] at = this.getTransition();
		CATransition[] finalTr = new CATransition[at.length];
		for(int i=0;i<finalTr.length;i++)
		{
			int[] in=at[i].getSource();
			int[] l=at[i].getLabelP();
			int[] f= at[i].getArrival();
			finalTr[i] = new CATransition(Arrays.copyOf(in,in.length),Arrays.copyOf(l,l.length),Arrays.copyOf(f,f.length));
		}
		
		int[][] nf = new int[finalstates.length][];
		for (int i=0;i<finalstates.length;i++)
			nf[i]=Arrays.copyOf(finalstates[i], finalstates[i].length);
		
		return new CA(rank,Arrays.copyOf(initial, initial.length),Arrays.copyOf(states, states.length),finalstates,finalTr);
	}
	
	/**
	 * compute the projection on the i-th principal, or null if rank=1
	 * @param i		index of the CA
	 * @return		the ith principal
	 */
	public CA proj(int i)
	{
		if ((i<0)||(i>rank)) //check if the parameter i is in the rank of the CA
			return null;
		CATransition[] tra = this.getTransition();
		int[] init = new int[1];
		init[0]=initial[i];
		int[] st= new int[1];
		st[0]= states[i];
		int[][] fi = new int[1][];
		fi[0]=finalstates[i];
		CATransition[] t = new CATransition[tra.length];
		int pointer=0;
		for (int ind=0;ind<tra.length;ind++)
		{
			CATransition tt= ((CATransition)tra[ind]);
			int label = tt.getLabelP()[i];
			if(label!=0)
			{
				int source =  tt.getSource()[i];
				int dest = tt.getArrival()[i];
				int[] sou = new int[1];
				sou[0]=source;
				int[] des = new int[1];
				des[0]=dest;
				int[] lab = new int[1];
				lab[0]=label;
				CATransition selected = new CATransition(sou,lab,des);
				boolean skip=false;
				for(int j=0;j<pointer;j++)
				{
					if (t[j].equals(selected))
					{
						skip=true;
						break;
					}
				}
				if (!skip)
				{
					t[pointer]=selected;
					pointer++;
				}
			}
		}
		
		tra = new CATransition[pointer];
		for (int ind=0;ind<pointer;ind++)
			tra[ind]=t[ind];
		return new CA(1,init,st,fi,tra);
	}
	
	/**
	 * compute the most permissive controller for strong agreement
	 * @return KS
	 */
	public CA smpc()
	{
		CA a = this.clone();
		CATransition[] t = a.getTransition();
		int removed=0;
		for (int i=0;i<t.length;i++)
		{
			if (!t[i].match())
			{
				t[i] = null;
				removed++;
			}
		}
		/**
		 * remove holes (null) in t
		 */
		int pointer=0;
		CATransition[] finalTr2 = new CATransition[t.length-removed];
		for (int ind=0;ind<t.length;ind++)
		{
			if (t[ind]!=null)
			{
				finalTr2[pointer]=t[ind];
				pointer++;
			}
		}

		a.setTransition(finalTr2);
		a = CAUtil.removeDanglingTransitions(a);
		a = CAUtil.removeUnreachable(a);
		return a;
	}
	
	/**
	 * compute the most permissive controller for agreement
	 * @return the most permissive controller for agreement
	 */
	public CA mpc()
	{
		CA a = this.clone();
		CATransition[] t = a.getTransition();
		int removed=0;
		for (int i=0;i<t.length;i++)
		{
			if (t[i].request())
			{
				t[i] = null;
				removed++;
			}
		}
		/**
		 * remove holes (null) in t
		 */
		int pointer=0;
		CATransition[] finalTr2 = new CATransition[t.length-removed];
		for (int ind=0;ind<t.length;ind++)
		{
			if (t[ind]!=null)
			{
				finalTr2[pointer]=t[ind];
				pointer++;
			}
		}
		a.setTransition(finalTr2);
		a = CAUtil.removeDanglingTransitions(a);
		a = CAUtil.removeUnreachable(a);
		return a;
	}
	
	/**
	 * 
	 * @return  true if the CA is strongly safe
	 */
	public boolean strongSafe()
	{
		CA at = this.clone();
		at = CAUtil.removeDanglingTransitions(at);
		at = CAUtil.removeUnreachable(at);
		CA a = this.smpc();
		return (a.getTransition().length == at.getTransition().length);
	}
	
	/**
	 * 
	 * @return true if the CA admits strong agreement
	 */
	public boolean strongAgreement()
	{
		if (this.strongSafe())
			return true;
		CA a = this.smpc();
		return (a.getTransition().length!=0);
	}
	
	/**
	 * 
	 * @return true if the CA is safe
	 */
	public boolean safe()
	{
		CA at = this.clone();
		at = CAUtil.removeDanglingTransitions(at);
		at = CAUtil.removeUnreachable(at);
		CA a = this.mpc();
		return (a.getTransition().length == at.getTransition().length);
	}
	
	/**
	 * 
	 * @return true if the CA admits agreement
	 */
    public boolean agreement()
    {
    	if (this.safe())
			return true;
		CA a = this.mpc();
		return (a.getTransition().length!=0);
    }
	
	/**
	 * 
	 * @return null if the branching condition holds, otherwise the initial state and label of the
	 * 				match transition together with the state where the transition is not present
	 */
	public int[][] branchingCondition()
	{
		/**
		 * for all transitions:
		 * 		if t is a match select the state s of the sender and the label l
		 * 			for all reachable states
		 * 				if the sender is in state s
		 * 					if there is no transition from the selected state with label l
		 * 						return false
		 */
		CATransition[] t = this.getTransition();
		int[][] reach = this.reachableStates();
		for (int i=0;i<t.length;i++)
		{
			if (t[i].match())
			{
				int[] l=t[i].getLabelP();
				int s = t[i].sender();
				for (int j=0;j<reach.length;j++)
				{
					if ((reach[j][s]==t[i].getSource()[s])&&(!Arrays.equals(reach[j],t[i].getSource())))
					{
						int z=0;
						boolean found = false;
						while ((!found)&&(z<t.length))
						{
							found=Arrays.equals(t[z].getSource(), reach[j])&&Arrays.equals(t[z].getLabelP(), l);
							z++;
						}
						if (!found)
						{
							int[][] re = new int[3][];
							re[0]=t[i].getSource();
							re[1]=t[i].getLabelP();
							re[2]=reach[j];
							return re;
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return null if the ext. branching condition holds, otherwise the initial state and label of the
	 * 				transition together with the state where the transition is not present
	 */
	public int[][] extendedBranchingCondition()
	{
		/**
		 * for all transitions:
		 * 		if t is a match select the state s of the sender and the label l
		 * 			for all reachable states
		 * 				if the sender is in state s
		 * 					if there is no transition from the selected state with label l
		 * 						return false
		 */
		CATransition[] t = this.getTransition();
		int[][] reach = this.reachableStates();
		for (int i=0;i<t.length;i++)
		{
			if (!t[i].request())
			{
				int[] l=t[i].getLabelP();
				int s = t[i].sender();
				for (int j=0;j<reach.length;j++)
				{
					if ((reach[j][s]==t[i].getSource()[s])&&(!Arrays.equals(reach[j],t[i].getSource())))
					{
						int z=0;
						boolean found = false;
						while ((!found)&&(z<t.length))
						{
							found=Arrays.equals(t[z].getSource(), reach[j])&&Arrays.equals(t[z].getLabelP(), l);
							z++;
						}
						if (!found)
						{
							int[][] re = new int[3][];
							re[0]=t[i].getSource();
							re[1]=t[i].getLabelP();
							re[2]=reach[j];
							return re;
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return the state if there are mixed choice, null otherwise
	 */
	public int[] mixedChoice()
	{
		/**
		 * for all transitions
		 * 		for all transitions
		 * 			if the two transitions have the same initial state and different sender return false
		 */
		CATransition[] t = this.getTransition();
		for (int i=0;i<t.length;i++)
		{
			for(int j=i+1;j<t.length;j++)
			{
				if (Arrays.equals(t[i].getSource(), t[j].getSource())&&t[i].sender()!=t[j].sender())
					return t[i].getSource();
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return all the reachable states 
	 */
	private int[][] reachableStates()
	{
		CA aut=this.clone();
		aut = CAUtil.removeUnreachable(aut);
		aut = CAUtil.removeDanglingTransitions(aut);
		int[][] s = new int[this.prodStates()][];
		s[0]=aut.getInitialCA();
		CATransition[] t = aut.getTransition();
		int pointer=1;
		for (int i=0;i<t.length;i++)
		{
			int[] p = t[i].getArrival();
			boolean found=false;
			int j=0;
			while((!found)&&(s[j]!=null))
			{
				found = Arrays.equals(p, s[j]);
				j++;
			}
			if (!found)
			{
				s[pointer]=p;
				pointer++;
			}
		}
	    int[][] f = new int[pointer][];
	    for (int i=0;i<pointer;i++)
	    	f[i]=s[i];
		return f;
	}
	
	/**
	 * 
	 * @return all the final states of the CA
	 */
	public  int[][] allFinalStates()
	{
//		if (rank==1)
//			return finalstates;
		int[] states=new int[finalstates.length];
		int comb=1;
		int[] insert= new int[states.length];
		for (int i=0;i<states.length;i++)
		{
			states[i]=finalstates[i].length;
			comb*=states[i];
			insert[i]=0;
		}
		int[][] modif = new int[comb][];
		int[] indstates = new int[1];
		indstates[0]= states.length-1;
		int[] indmod = new int[1];
		indmod[0]= 0; 
		//CAUtil.recGen(finalstates, modif,  states, 0, states.length-1, insert);
		CAUtil.recGen(finalstates, modif,  states, indmod, indstates, insert);
		return modif;
	}
	
	/**
	 * 
	 * @return the liable transitions
	 */
	public CATransition[] liable()
	{
		CA aut = this.clone();
		aut = this.mpc();
		int[][] ms = aut.reachableStates();
		CATransition[] t = this.getTransition();
		CATransition[] liable = new CATransition[t.length];
		int pointliable=0;
		for (int i=0;i<t.length;i++)
		{
			int[] s = t[i].getSource();
			int[] d = t[i].getArrival();
			boolean founds=false;
			boolean foundd=false;
			for(int j=0;j<ms.length;j++)
			{
				if (Arrays.equals(s,ms[j]))
					founds=true;
				if (Arrays.equals(d, ms[j]))
					foundd=true;
			}
			if (founds&&!foundd)
			{
				liable[pointliable]=t[i];
				pointliable++;
			}
		}
		CATransition[] ret = new CATransition[pointliable];
		for(int i=0;i<pointliable;i++)
			ret[i]=liable[i];
		return ret;
	}
	
	/**
	 * 
	 * @return the strongly liable transitions
	 */
	public CATransition[] strongLiable()
	{
		CA k = this.clone();
		k = k.smpc();
		int[][] ms = k.reachableStates();
		CATransition[] t = this.getTransition();
		CATransition[] liable = new CATransition[t.length];
		int pointliable=0;
		for (int i=0;i<t.length;i++)
		{
			int[] s = t[i].getSource();
			int[] d = t[i].getArrival();
			boolean founds=false;
			boolean foundd=false;
			for(int j=0;j<ms.length;j++)
			{
				if (Arrays.equals(s,ms[j]))
				{
					founds=true;
				}
				if (Arrays.equals(d, ms[j]))
				{
					foundd=true;
				}
			}
			if (founds&&!foundd)
			{
				liable[pointliable]=t[i];
				pointliable++;
			}
		}
		CATransition[] ret = new CATransition[pointliable];
		for(int i=0;i<pointliable;i++)
			ret[i]=liable[i];
		return ret;
	}
}
