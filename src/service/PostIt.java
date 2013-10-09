package service;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import json.JSONFactory;

import persistence.PersistenceService;
import util.SendMail;

/**
 * Servlet implementation class PostIt
 */
@WebServlet("/PostIt")
public class PostIt extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private int maxReturn=30;
    private String un="rd@uvic.ca";
    private String pw="8675309";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostIt() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String op=request.getParameter("op");
		PersistenceService.getInstance().initialize();
		if(op.equals("postquestion")){
			System.out.println("post Question");
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			if(username!=null||password!=null){
				if(username.equals(this.un) && password.equals(this.pw)){
					/*
					 * get db results and post as response variable
					 */
					String question=request.getParameter("question");
					PersistenceService.getInstance().insertQuestion(question);
					PersistenceService.getInstance().showTable("questions");
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/viewquestions.jsp");
		            dispatcher.forward(request, response);
					response.getWriter().close();
				}
			}else{
				/*
				 * Send email to administrator
				 */
				System.out.println("Send email to ron@yakkit.com for verification ");
				SendMail sm = new SendMail();
				String requestMsg = new String("");
				//String ques = parseQuery(request.getQueryString());
				String ques = request.getQueryString();
				requestMsg+="Question: "+request.getParameter("question")+"\n";
				requestMsg+="________\n";
				requestMsg+="To accept Message click link below , else delete email\n";
				//requestMsg+="http://web.yakkit.com/YakkitPostIt/PostIt?op=postquestion&question="+ques+"&un="+un+"&pw="+pw;
				requestMsg+="http://web.yakkit.com/YakkitPostIt/PostIt?"+ques+"&username="+un+"&password="+pw;
				sm.sendmail("Request Question Permission", requestMsg);
				request.setAttribute("servletmessage", "your question has been submitted for moderation");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/viewquestions.jsp");
	            dispatcher.forward(request, response);
				response.getWriter().close();
			}
		}else if(op.equals("searchquestions")){
			String searchKey=request.getParameter("searchkey");
			int startIndex=Integer.valueOf(request.getParameter("startindex"));
			LinkedList<Questions> ques = PersistenceService.getInstance().getQuestions(searchKey, startIndex, maxReturn);
			LinkedList<Questions> ques1=(LinkedList<Questions>) this.reverse(ques);
			response.getWriter().print(JSONFactory.getInstance().createJSONQuestion(ques1,startIndex));
		}else if(op.equals("searchanswers")){
			//url=url+"?op=searchanswers&searchkey="+searchvar+"&startindex=0&questionkey="+questionid;
			String searchKey=request.getParameter("searchkey");
			int startIndex=Integer.valueOf(request.getParameter("startindex"));
			int quesid = Integer.valueOf(request.getParameter("questionkey"));
			LinkedList<Answers> ans = PersistenceService.getInstance().getAnswers(searchKey, startIndex, quesid, maxReturn);
			ans=(LinkedList<Answers>) this.reverse(ans);
			response.getWriter().print(JSONFactory.getInstance().createJSONAnswer(ans,startIndex));
		}else if(op.equals("postanswer")){
			System.out.println("post Answer");
			
			/*
			 * get db results and post as response variable
			 */
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			if(username!=null||password!=null){
				String answer=request.getParameter("answer");
				int quesid = Integer.valueOf(request.getParameter("questionid"));
				double lat = Double.valueOf(request.getParameter("lat"));
				double lng = Double.valueOf(request.getParameter("lng"));
				
				PersistenceService.getInstance().insertAnswer(quesid,answer,lat,lng);
				PersistenceService.getInstance().showTable("answers");
				
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/viewanswers.jsp");
	            dispatcher.forward(request, response);
				response.getWriter().close();
			}else{
				/*
				 * Send email to administrator
				 */
				System.out.println("Send email to ron@yakkit.com for verification ");
				SendMail sm = new SendMail();
				String requestMsg = new String("");
				//String ques = parseQuery(request.getQueryString());
				String ques = request.getQueryString();
				requestMsg+="Answer: "+request.getParameter("answer")+"\n";
				requestMsg+="________\n";
				requestMsg+="To accept Message click link below , else delete email\n";
				//requestMsg+="http://web.yakkit.com/YakkitPostIt/PostIt?op=postquestion&question="+ques+"&un="+un+"&pw="+pw;
				requestMsg+="http://web.yakkit.com/YakkitPostIt/PostIt?"+ques+"&username="+un+"&password="+pw;
				request.setAttribute("servletmessage", "your answer has been submitted for moderation");
				sm.sendmail("Request Ansswer Permission", requestMsg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/viewanswers.jsp");
	            dispatcher.forward(request, response);
				response.getWriter().close();
			}
		}else if(op.equals("removeanswer")){
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			if(username.equals(this.un) && password.equals(this.pw)){
				System.out.println("remove Answer");
				int ansid = Integer.valueOf(request.getParameter("answerid"));
				PersistenceService.getInstance().deleteAnswer(ansid);
				response.getWriter().print("success");
				response.getWriter().close();
			}else{
				response.getWriter().print("wrong un or pw");
				response.getWriter().close();
			}
		}else if(op.equals("removequestion")){
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			if(username.equals(this.un) && password.equals(this.pw)){
				System.out.println("remove Question");
				int quesid = Integer.valueOf(request.getParameter("questionid"));
				PersistenceService.getInstance().deleteQuestion(quesid);
				LinkedList<Answers> ans = PersistenceService.getInstance().getAnswers("", 0, quesid, 1000);
				for(int i=0;i<ans.size();i++){
					int ansid = ans.get(i).id;
					PersistenceService.getInstance().deleteAnswer(ansid);
				}
				response.getWriter().print("success");
				response.getWriter().close();
			}else{
				response.getWriter().print("wrong un or pw");
				response.getWriter().close();
			}
		}else{
			response.getWriter().print("Unkown Operation "+op);
			response.getWriter().close();
		}
	}

	/*private String parseQuery(String queryString) {
		int startIndex = queryString.indexOf("question=");
		startIndex+=9;
		int stopIndex = queryString.indexOf("&op=");
		return queryString.substring(startIndex, stopIndex);
	}*/

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	private LinkedList<?> reverse(LinkedList<?> list){
		LinkedList revList = new LinkedList();
		for(int i=0;i<list.size();i++){
			Object o = list.get(list.size()-i-1);
			revList.add(o);
		}
		return revList;
	}
}
