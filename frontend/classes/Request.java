/*  Project: Vehicular Cloud Real Time System (VCRTS)
 * Class: Request.java
 * Authors: Group 2 (Justin Cracchiolo, Lauren Rodriguez, David Choi, Tristan Huertas, Ivan Lin, Anthony Vallejo, Sebastian Villavicencio)
 * Date: March 2026
 * This program represents a request in the VCRTS system. 
 * It contains the type of request (job or vehicle), the output stream to send responses back to the requester, and the decision made regarding the request.
 */
package classes;

import java.io.DataOutputStream;

public class Request {

    public String request; //job or vehicle
    public DataOutputStream out;
    public String decision;
}
