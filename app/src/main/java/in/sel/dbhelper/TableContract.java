/*Most of The column are self Explainable by Skipping that only few 
 * columns description I am maintaining*/

package in.sel.dbhelper;


public class TableContract {

	public static final String AUTO_ID = "_id";
	
	/* Data Type And Separator */
	private static final String TYPE_INTEGER = " INTEGER ";
	private static final String TYPE_BOOLEAN = " BOOLEAN ";
	private static final String TYPE_TEXT = " TEXT ";
	private static final String SEP_COMMA = " , ";

	private static final String CLOSE_BRACE = " ) ";
	private static final String OPEN_BRACE = " ( ";
	private static final String SEMICOLON = " ; ";

	private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";
	private static final String AUTO_INCREMENT = " AUTOINCREMENT ";
	private static final String CREATE_TABLE = " CREATE TABLE ";
	private static final String PRIMARY_KEY = " PRIMARY KEY ";
	private static final String NOT_NULL = " NOT NULL ";

	/* Constraints */
	private static final String ON_CONFLICT_REPLACE = " ON CONFLICT REPLACE ";
	private static final String ON_CONFLICT_IGNORE = " ON CONFLICT IGNORE ";
	private static final String UNIQUE = " UNIQUE ";
	
	/* Application Related Table */
	public interface TimeStamp {
		String TABLE_NAME = "TimeStamp";
		String CTABLE_NAME = "TableName";
		String CTIME_STAMP = "TimeStamp";
		String AUTO_ID = "_id";

		String SQL_CREATE = CREATE_TABLE + TABLE_NAME 
				+ OPEN_BRACE
		           + AUTO_ID + TYPE_INTEGER+PRIMARY_KEY+AUTO_INCREMENT+SEP_COMMA
		           + CTABLE_NAME + TYPE_TEXT + SEP_COMMA 
				   + CTIME_STAMP + TYPE_TEXT + SEP_COMMA 
				   + UNIQUE 
				     + OPEN_BRACE 
				       + CTABLE_NAME 
				     + CLOSE_BRACE + ON_CONFLICT_IGNORE 
				+ CLOSE_BRACE;

		String SQL_DROP =  DROP_TABLE+ TABLE_NAME;
	}

	/*String List Of State*/
	public interface Name
	{
		String TABLE_NAME = "BabyName";
		String AUTO_ID = "_id";
		String NAME_EN = "NameEn";
		String NAME_MA = "NameMa";
		String NAME_FRE = "NameFre";
		String GENDER_CAST = "Gender_Cast";/* */
		
		
		String SQL_CREATE = CREATE_TABLE+TABLE_NAME
				    +OPEN_BRACE
				    +AUTO_ID + TYPE_INTEGER+PRIMARY_KEY+AUTO_INCREMENT+SEP_COMMA
				    +NAME_EN+TYPE_TEXT+SEP_COMMA
				    +NAME_MA+TYPE_TEXT+SEP_COMMA
				    +NAME_FRE+TYPE_INTEGER+SEP_COMMA
				    +GENDER_CAST+TYPE_TEXT+SEP_COMMA
				    + UNIQUE 
				     + OPEN_BRACE 
				       + NAME_EN+SEP_COMMA
				       + NAME_MA
				     + CLOSE_BRACE + ON_CONFLICT_IGNORE
				    +CLOSE_BRACE;
		String SQL_DROP = DROP_TABLE+TABLE_NAME;
	}
	
	/** Saved Status Of*/
	public interface FavourateName
	{
		String TABLE_NAME = "favourite";
		String AUTO_ID = "_id";
		String NAME_EN = "NameEn";
		String NAME_MA = "NameMa";
		String NAME_FRE = "NameFre";
		String GENDER_CAST = "Gender_Cast";
		String NAME_ID = "Name_id";
		String IS_ACTIVE = "is_active";


		String SQL_CREATE = CREATE_TABLE+TABLE_NAME
			    +OPEN_BRACE
			    +AUTO_ID + TYPE_INTEGER+PRIMARY_KEY+AUTO_INCREMENT+SEP_COMMA
			    +NAME_EN+TYPE_TEXT+SEP_COMMA
			    +NAME_MA+TYPE_TEXT+SEP_COMMA
			    +NAME_FRE+TYPE_INTEGER+SEP_COMMA
				+GENDER_CAST+TYPE_TEXT+SEP_COMMA
				+NAME_ID+TYPE_INTEGER+SEP_COMMA
				+IS_ACTIVE+TYPE_INTEGER+SEP_COMMA
			    + UNIQUE 
			     + OPEN_BRACE 
			       + NAME_ID
			     + CLOSE_BRACE + ON_CONFLICT_REPLACE
			    +CLOSE_BRACE;
	String SQL_DROP = DROP_TABLE+TABLE_NAME;
	}

	public interface CountValue
	{
		String TABLE_NAME = "CountValue";
		String AUTO_ID = "_id";
		String ALPHABET="Alphabet";
		String ALPH_COUNT="AlphCount";
		String MALE_COUNT="MaleCount";
		String FEMALE_COUNT="FemaleCount";
		String TRANS_COUNT="TransCount";
		String DELETE_DCOUNT="DeletedCount";
		String SIR_COUNT="SirCount" ;

		String SQL_CREATE = CREATE_TABLE+TABLE_NAME
				+OPEN_BRACE
				+AUTO_ID + TYPE_INTEGER+PRIMARY_KEY+AUTO_INCREMENT+SEP_COMMA
				+ALPHABET+TYPE_TEXT+SEP_COMMA
				+ALPH_COUNT+TYPE_INTEGER+SEP_COMMA
				+MALE_COUNT+TYPE_INTEGER+SEP_COMMA
				+FEMALE_COUNT+TYPE_INTEGER+SEP_COMMA
				+TRANS_COUNT+TYPE_INTEGER+SEP_COMMA
				+DELETE_DCOUNT+TYPE_INTEGER+SEP_COMMA
				+SIR_COUNT+TYPE_INTEGER+SEP_COMMA
				+ UNIQUE
				+ OPEN_BRACE
				+ ALPHABET
				+ CLOSE_BRACE + ON_CONFLICT_REPLACE
				+CLOSE_BRACE;

		String SQL_DROP = DROP_TABLE+TABLE_NAME;
	}
}
