package com.octavianonline.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * The get method handles a request for previously saved data, and the post method for writing new data
 */
@WebServlet("/")
public class MainServlet extends HttpServlet {

    private Long credit;
    private Double sound;
    private String language;

    private FileOutputStream fos;
    private ObjectOutputStream oos;

    private final static String PARAMETER_FILE_NAME = "parameters.data";
    private final static String LANGUAGE_PARAMETER = "language";
    private final static String SOUND_PARAMETER = "sound";
    private final static String CREDIT_PARAMETER = "credit";
    private String message;


    public MainServlet(){
        super();
    }

    /**
     * Request for getting previously saved data
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {


        message = "";
        /**
         * Serialized Parameters File
         */
        FileInputStream fis = new FileInputStream(this.getClass().getClassLoader().getResource(PARAMETER_FILE_NAME).getFile());

        try {
            /**
             * Retrieving data from a file
             */
            ObjectInputStream oin = new ObjectInputStream(fis);

            try {
                Parameters ts = (Parameters) oin.readObject();
                req.setAttribute(LANGUAGE_PARAMETER, ts.getLanguage());
                language = ts.getLanguage();
                req.setAttribute(SOUND_PARAMETER, ts.getSound());
                sound = ts.getSound();
                req.setAttribute(CREDIT_PARAMETER, ts.getCredit());
                credit = ts.getCredit();

                message="Successful reading of parameters";

                /**
                 *Check if the request was sent from the browser
                 * If the request was sent from the browser, then we form the page for the browser, otherwise send the answer in json
                 */
                if (req.getParameter("webpage") != null) {
                    req.getRequestDispatcher("parameters.jsp").forward(req, resp);
                } else {
                    resp.getWriter().write("{\n" +
                            "   \"" + LANGUAGE_PARAMETER + "\": " + "\""+ language + "\",\n" +
                            "   \"" + SOUND_PARAMETER + "\": " +"\""  + sound + "\",\n" +
                            "   \"" + CREDIT_PARAMETER + "\": " + credit + ",\n" +
                            "   \"" + "message" + "\": " +"\"" + message +"\"" +"\n" +
                            "}");
                    resp.getWriter().flush();
                    resp.getWriter().close();
                }

            } catch (ClassNotFoundException e) {
                message = "Error reading data file";
                getErrorMessage(req, resp);
            } finally {
                fis.close();
                oin.close();
            }

        } catch (EOFException e) {
            message = "Data not found";
            getErrorMessage(req, resp);
        }

    }

    /**
     * Saving new data from user in file
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        message = "";
        /**
         * Map with received parameters for saving
         */
        Map<String, String[]> parameters = req.getParameterMap();

        if (parameters.size() > 0) {
            String[] creditParameter = parameters.get("credit");
            String[] soundParameter = parameters.get("sound");
            String[] languageParameter = parameters.get("language");

            if (creditParameter != null && !creditParameter[0].isEmpty() && soundParameter != null && !soundParameter[0].isEmpty() && languageParameter != null && !languageParameter[0].isEmpty()) {
                try {
                    Long credit = Long.parseLong(creditParameter[0]);
                    Double sound = Double.parseDouble(soundParameter[0]);
                    String language = languageParameter[0];
                    Parameters newParameters = new Parameters(credit, sound, language);

                    /**
                     * Serialize and write new data to a file
                     */
                    try {
                        String parameterFile = this.getClass().getClassLoader().getResource(PARAMETER_FILE_NAME).getFile();
                        fos = new FileOutputStream(parameterFile);
                        oos = new ObjectOutputStream(fos);
                        oos.writeObject(newParameters);
                        oos.flush();
                        message = "parameters saved successfully";
                        sendSuccessfulSaveReply(req, resp);
                    } catch (IOException e) {
                        message = "Error writing parameters to file";
                        getErrorMessage( req, resp);
                    } finally {
                        fos.close();
                        oos.close();
                    }


                } catch (Exception e) {
                    message = "Invalid parameters format";
                    getErrorMessage( req, resp);
                }
            } else {
                message = "Invalid request parameters";
                getErrorMessage( req, resp);
            }


        } else {
            message = "invalid request format";
            getErrorMessage( req, resp);
        }
    }

    /**
     * Error message while reading or writing data to a file
     * @param req
     * @param resp
     * @throws IOException
     * @throws ServletException
     */
    private void getErrorMessage(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (req.getParameter("webpage") != null) {
            req.setAttribute("message", message);
            req.getRequestDispatcher("parameters.jsp").forward(req, resp);
        } else {
            resp.getWriter().write(
                    "{\n   \"message\":" + "\"" + message + "\"" + "\n}");
            resp.getWriter().flush();
            resp.getWriter().close();
        }
    }

    /**
     * Response about successfully writing new data to a file
     * @param req
     * @param resp
     * @throws IOException
     * @throws ServletException
     */
    private void sendSuccessfulSaveReply(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (req.getParameter("webpage") != null) {
            req.setAttribute("message", message);
            req.setAttribute(LANGUAGE_PARAMETER, req.getParameter(LANGUAGE_PARAMETER));
            req.setAttribute(SOUND_PARAMETER, req.getParameter(SOUND_PARAMETER));
            req.setAttribute(CREDIT_PARAMETER, req.getParameter(CREDIT_PARAMETER));
            req.getRequestDispatcher("parameters.jsp").forward(req, resp);
        } else {
            resp.getWriter().write(
                    "{\n   \"message\":" + "\"" + message + "\"" + ",\n" +
                            "   \"" + LANGUAGE_PARAMETER + "\": " + "\"" + req.getParameter(LANGUAGE_PARAMETER) + "\"" + ",\n" +
                            "   \"" + SOUND_PARAMETER + "\": " + "\"" + req.getParameter(SOUND_PARAMETER) + "\"" + ",\n" +
                            "   \"" + CREDIT_PARAMETER + "\": " + req.getParameter(CREDIT_PARAMETER) + "\n" +
                            "}");
            resp.getWriter().flush();
            resp.getWriter().close();
        }
    }

}