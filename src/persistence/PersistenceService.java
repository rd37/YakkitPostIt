package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Random;

import service.Answers;
import service.Questions;


public class PersistenceService {
	private static PersistenceService database = new PersistenceService();

	private PersistenceService(){}

	public static PersistenceService getInstance(){return database;};

	private Random random;
	/* the default framework is embedded*/
    //private String framework = "embedded";
    private String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private String protocol = "jdbc:derby:";
    
    private String litToolDB = "Postits4";
    private Connection conn;
    
    private PreparedStatement questionInsert = null;
    private PreparedStatement answerInsert = null;
    private PreparedStatement questionDelete = null;
    private PreparedStatement answerDelete = null;
    private PreparedStatement questionUpdate = null;
    private PreparedStatement answerUpdate = null;
    
    private LinkedList<Statement> sqlStatements = new LinkedList<Statement>();
    private static boolean initialized=false;
    private int index=0;
	/*
	 * check if tables exist
	 * if so, then create groups,products,literature review, and reviews
	 * if not,
	 * then done.
	 */
	public void initialize(){
		if(initialized){
			return;
		}else{
			initialized=true;
		}
		/* 
		 * load the desired JDBC driver 
		 * Starts the derby service, but not database
		 * */
        loadDriver();
        try{
        	Properties props = new Properties(); // connection properties
            props.put("user", "yakittool");
            props.put("password", "yakittool@929!");
            conn = DriverManager.getConnection(protocol + litToolDB
                    + ";create=true", props);
            System.out.println("Connected to and created database " + litToolDB);
            conn.setAutoCommit(false);
            Statement s = conn.createStatement();
            
            try{
            	s.execute("create table questions( id int primary key,  question varchar(140)  ) ");
            	s.execute("create table answers( id int primary key,  questionid int, answer varchar(140) , lat double , lng double ) ");
            	         	
            }catch(Exception e){
            	System.out.println("Tables created, carry on");
            }
            sqlStatements.add(s);//sqlStatements.add(s1);
            
            questionInsert = conn.prepareStatement("insert into questions values (? , ? )");
            answerInsert = conn.prepareStatement("insert into answers values (? , ? , ? , ? , ?)");
            questionDelete = conn.prepareStatement("delete from questions where id=?");
            answerDelete = conn.prepareStatement("delete from answers where id=?");
            questionUpdate = conn.prepareStatement("update questions set question = ? where id = ?");
            answerUpdate = conn.prepareStatement("update answers set answer = ?, lat = ? , lng = ? where questionid = ?");
            
        }catch(Exception e){
        	System.out.println("Unable to initialize database "+e);
        	e.printStackTrace();
        }
        int randSeed = (int)(System.currentTimeMillis()%10000000);
        System.out.println("Set random seed "+randSeed);
        random=new Random(randSeed);
	}
	
	public void insertQuestion(String question){
		System.out.println("Persist Add question "+question);
		try{
			questionInsert.setInt(1, random.nextInt() );
			questionInsert.setString(2, question );
			questionInsert.executeUpdate();
			conn.commit();
		}catch(Exception e){
			System.out.println("Error:"+e);
		}
	}
	
	public void insertAnswer(int questionId , String answer, double lat, double lng){
		System.out.println("Persist answer");
		try{
			answerInsert.setInt(1, random.nextInt() );
			answerInsert.setInt(2, questionId );
			answerInsert.setString(3, answer);
			answerInsert.setDouble(4, lat );
			answerInsert.setDouble(5, lng);
			answerInsert.execute();
			conn.commit();
		}catch(Exception e){
			System.out.println("Answer Error:"+e);
		}
	}
	
	public void updateQuestion(int questionId, String question){
		System.out.println("Update Question");
		try{
			questionUpdate.setString(1, question);
			questionUpdate.setInt(2, questionId);
			questionUpdate.execute();
			conn.commit();
		}catch(Exception e){
			System.out.println("Update Question Error:"+e);
		}
	}
	
	public void updateAnswer(int questionId,String answer, double lat, double lng){
		try{
			System.out.println("update anser "+questionId+":"+lat+":"+lng);
			answerUpdate.setString(1, answer);
			answerUpdate.setDouble(2, lat);
			answerUpdate.setDouble(3, lng);
			answerUpdate.setInt(4, questionId);
			answerUpdate.execute();
			conn.commit();
			
		}catch(Exception e){
			System.out.println("Errors ok:"+e);
			e.printStackTrace();
		}
	}	
	
	public void deleteQuestion(int questionId){
		System.out.println("delete Question");
		try{
			questionDelete.setInt(1, questionId);
			questionDelete.executeUpdate();
			conn.commit();
		}catch(Exception e){
			System.out.println("Error removing question id "+questionId);
			e.printStackTrace();
		}
	}
	
	public void deleteAnswer(int answerId){
		System.out.println("delelte ansswer ");
		try{
			answerDelete.setInt(1, answerId);
			answerDelete.executeUpdate();
			conn.commit();
		}catch(Exception e){
			System.out.println("Error removing answer id "+answerId);
			e.printStackTrace();
		}
	}
	

	private void loadDriver() {
        try {
        	System.out.println("Try to load driver "+driver);
            Class.forName(driver).newInstance();
            System.out.println("Loaded the appropriate driver");
        } catch (ClassNotFoundException cnfe) {
            System.err.println("\nUnable to load the JDBC driver " + driver);
            System.err.println("Please check your CLASSPATH.");
            cnfe.printStackTrace(System.err);
        } catch (InstantiationException ie) {
            System.err.println(
                        "\nUnable to instantiate the JDBC driver " + driver);
            ie.printStackTrace(System.err);
        } catch (IllegalAccessException iae) {
            System.err.println(
                        "\nNot allowed to access the JDBC driver " + driver);
            iae.printStackTrace(System.err);
        }
    }
	
	public LinkedList<Answers> getAnswers(String searchKey, int startIndex,
			int quesid, int maxReturn) {
		LinkedList<Answers> list = new LinkedList<Answers>();
		try {
			
			System.out.println("Try to get Answers with "+searchKey+" and "+quesid);
			ResultSet set = this.sqlStatements.get(0).executeQuery("select * from answers where questionid = "+quesid+" and LOWER(answer) like LOWER('%"+searchKey+"%')");
			
			System.out.println("Try to get Answers with "+searchKey+" result size ");
			int count=0;
			while(set.next() ){
				//System.out.println("|"+set.getInt(1)+"\t|"+set.getString(2)+"\t|" );
				if(startIndex<=count && list.size()<maxReturn){
					//System.out.println("|"+set.getInt(1)+"\t|"+set.getString(2)+"\t|" );
					list.add( new Answers(set.getInt("id"),set.getString("answer"),set.getDouble("lat"),set.getDouble("lng")) );
				}
				count++;
			}
			set.close();
			if(list.size()>0)
				list.get(0).count=count;
			System.out.println("done getting answers for "+searchKey+" count "+count);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public LinkedList<Questions> getQuestions(String searchKey,int startIndex,int maxList){
		LinkedList<Questions> list = new LinkedList<Questions>();
		try {
			
			System.out.println("Try to get Questions wiht "+searchKey);
			ResultSet set = this.sqlStatements.get(0).executeQuery("select * from questions where LOWER(question) like LOWER('%"+searchKey+"%')");
			
			System.out.println("Try to get Questions wiht "+searchKey+" result size ");
			int count=0;
			while(set.next() ){
				//System.out.println("|"+set.getInt(1)+"\t|"+set.getString(2)+"\t|" );
				if(startIndex<=count && list.size()<maxList){
					//System.out.println("|"+set.getInt(1)+"\t|"+set.getString(2)+"\t|" );
					list.add( new Questions(set.getInt(1),set.getString(2)) );
				}
				count++;
			}
			set.close();
			if(list.size()>0)
				list.get(0).count=count;
			System.out.println("done getting question for "+searchKey+" count "+count);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public void showTable(String type){
		try{
			if(type.equals("questions")){
				ResultSet set = this.sqlStatements.get(0).executeQuery("select * from "+type);
				System.out.println("****************"+type+"****************");
				System.out.println("| questionId \t| question \t| ");
				while(set.next()){ 
					System.out.println("|"+set.getInt(1)+"\t|"+set.getString(2)+"\t|" );
				}
				System.out.println("**************************************");
				set.close();
				
			}else if(type.equals("answers")){
				ResultSet set = this.sqlStatements.get(0).executeQuery("select * from "+type);
				System.out.println("****************"+type+"****************");
				System.out.println("| answerId \t questionid \t| answer \t| lat \t| lng \t|");
				while(set.next()){ 
					System.out.println("|"+set.getInt(1)+"\t|"+set.getInt(2)+"\t|"+set.getString(3)+"\t|"+set.getInt(4)+"\t|"+set.getInt(5)+"\t|" );
				}
				System.out.println("**************************************");
				set.close();
			}
		}catch(Exception e){
			System.out.println("error showing talbe "+e);
			e.printStackTrace();
		}
	}

	
}