package edu.miu.cs.controller;

import edu.miu.cs.model.Quiz;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/quiz")
public class QuizServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Quiz quiz;
        if (session.getAttribute("quiz") == null) {
            quiz = new Quiz();
        } else {
            String answer = req.getParameter("answer");
            quiz = (Quiz) session.getAttribute("quiz");
            quiz.checkAnswer(answer);
        }
        session.setAttribute("quiz", quiz);

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        String question = quiz.getNextQuestion();
        out.println("""
                <style>
                    body {
                        width: 20%;
                        margin: 0 auto;
                        border: 1px solid #000;
                        padding: 10px;
                        max-height: 20em;
                    }
                                    
                    input[type="number"] {
                        width: 15%;
                    }
                                    
                    input {
                        padding: 5px;
                    }
                    
                    input[type="submit"] {
                        border-radius: 3px;
                        margin-top: 1em;
                    }
                </style>
                """);
        out.println("<h1>The Number Quiz</h1>");
        out.println("<p>Your current score: </p>" + quiz.getScore());

        if (question == null) {
            out.println("<p>You have complete the quiz, with a score "+quiz.getScore()+" out of "+quiz.getTotalScore()+"</p>");
        } else {
            out.println("<p>Guess the next number in the sequence.</p>");
            out.println("<p>" + question + "</p>");
            out.println("""
                    <form action='quiz' method='post'>
                        Your answer is:
                        <input type='number' name='answer'/><br/>
                        <input type='submit' value='Submit'/>
                    </form>""");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
