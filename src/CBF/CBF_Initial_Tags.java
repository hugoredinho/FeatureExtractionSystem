package CBF;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.*;
import cc.mallet.types.*;
import AuxiliarFiles.*;
import CBF.CBF_Initial.TxtFilter;

public class CBF_Initial_Tags {

	
	static String sourceFolder = "src/Origem_POS/";
	//static final int withTags = 1;
	//static final int onlyTags = 2;
	Pipe pipe;
	String arg1,arg2,arg3;

	public CBF_Initial_Tags(String sourceFolder1, String arg1, String arg2, String arg3) throws IOException {
		if(sourceFolder1 != null && !sourceFolder1.isEmpty()) {
			sourceFolder = sourceFolder1;				
		}
		else {
			sourceFolder = "src/Origem_POS/";
		}
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.arg3 = arg3;
		pipe = buildPipe();
		System.out.println(sourceFolder);
		InstanceList instances = readDirectory(new File(sourceFolder));
		SaveInstancesInFileT sinstances = new SaveInstancesInFileT(instances, arg3);
		if (arg3.equals("freq")) {
			sinstances.execute_freq(arg1,arg2);
		} else if (arg3.equals("bool")) {
			sinstances.execute_bool(arg1,arg2);
		} else if (arg3.equals("tfidf")) {
			sinstances.execute_tfidf(arg1,arg2);
		} else if (arg3.equals("norm")) {
			sinstances.execute_norm(arg1,arg2);
		}
	}
	
	
	
	
	

	@SuppressWarnings("unchecked")
	public Pipe buildPipe() {
		ArrayList pipeList = new ArrayList();

		// Read data from File objects
		pipeList.add(new Input2CharSequence("UTF-8"));

		// Regular expression for what constitutes a token.
		// This pattern includes Unicode letters, Unicode numbers,
		// and the underscore character. Alternatives:
		// "\\S+" (anything not whitespace)
		// "\\w+" ( A-Z, a-z, 0-9, _ )
		// "[\\p{L}\\p{N}_]+|[\\p{P}]+" (a group of only letters and numbers OR
		// a group of only punctuation marks)
		Pattern tokenPattern = Pattern.compile("[\\p{L}_]+\\w+");

		// Tokenize raw strings
		pipeList.add(new CharSequence2TokenSequence(tokenPattern));

		// Normalize all tokens to all lowercase
		pipeList.add(new TokenSequenceLowercase());
			
		
		
		// Remove stopwords from a standard English stoplist.
		// options: [case sensitive] [mark deletions]
		if ((this.arg2.equals("sw")) || (this.arg2.equals("st_sw"))) {
			pipeList.add(new TokenSequenceRemoveStopwords(false, false));

		}
//		pipeList.add(new TokenSequenceStemming());
		//pipeList.add(new TokenSequenceRemoveStopwords(false, false));
		//pipeList.add(new TokenSequenceStemming());
		// stemming
		if ((this.arg2.equals("st")) || (this.arg2.equals("st_sw"))) {
			pipeList.add(new TokenSequenceStemming());
		}

		// n-gramas
		if (this.arg1.equals("unig")) {
			pipeList.add(new TokenSequenceNGrams(new int[] { 0, 1 }));
		}
		else if (this.arg1.equals("big")) {
			pipeList.add(new TokenSequenceNGrams(new int[] { 0, 2 }));
		} else if (this.arg1.equals("trig")) {
			pipeList.add(new TokenSequenceNGrams(new int[] { 0, 3 }));
		} else if (this.arg1.equals("4grams")) {
			pipeList.add(new TokenSequenceNGrams(new int[] { 0, 4 }));
		} else if (this.arg1.equals("5grams")) {
			pipeList.add(new TokenSequenceNGrams(new int[] { 0, 5 }));
		}

		// Rather than storing tokens as strings, convert
		// them to integers by looking them up in an alphabet.
		pipeList.add(new TokenSequence2FeatureSequence());

		// Do the same thing for the "target" field:
		// convert a class label string to a Label object,
		// which has an index in a Label alphabet.
		pipeList.add(new Target2Label());

		// Now convert the sequence of features to a sparse vector,
		// mapping feature IDs to counts.
		pipeList.add(new FeatureSequence2FeatureVector());

		// Print out the features and the label
		// pipeList.add(new PrintInputAndTarget());

		return new SerialPipes(pipeList);
	}

	public InstanceList readDirectory(File directory) {
		return readDirectories(new File[] { directory });
	}

	public InstanceList readDirectories(File[] directories) {

		// Construct a file iterator, starting with the
		// specified directories, and recursing through subdirectories.
		// The second argument specifies a FileFilter to use to select
		// files within a directory.
		// The third argument is a Pattern that is applied to the
		// filename to produce a class label. In this case, I've
		// asked it to use the last directory name in the path.
		FileIterator iterator = new FileIterator(directories, new TxtFilter(),
				FileIterator.LAST_DIRECTORY);

		// Construct a new instance list, passing it the pipe
		// we want to use to process instances.
		InstanceList instances = new InstanceList(pipe);

		// Now process each instance provided by the iterator.
		instances.addThruPipe(iterator);

		return instances;
	}



	/** This class illustrates how to build a simple file filter */
	class TxtFilter implements FileFilter {

		/**
		 * Test whether the string representation of the file ends with the
		 * correct extension. Note that {@ref FileIterator} will only call this
		 * filter if the file is not a directory, so we do not need to test that
		 * it is a file.
		 */
		public boolean accept(File file) {
			return file.toString().endsWith(".txt");
		}
	}
	
	
}
