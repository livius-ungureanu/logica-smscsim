/*
 * Copyright (c) 1996-2001
 * Logica Mobile Networks Limited
 * All rights reserved.
 *
 * This software is distributed under Logica Open Source License Version 1.0
 * ("Licence Agreement"). You shall use it and distribute only in accordance
 * with the terms of the License Agreement.
 *
 */
package com.logica.smscsim;

import com.logica.smpp.pdu.Request;
import com.logica.smpp.pdu.Response;

/**
 * <code>PDUProcessor</code> is abstract class which defines interface
 * functions for processing PDUs received by <code>SMSCSession</code>
 * from client as well as functions for the server side sending of PDU.
 * The actual implementation of the abstract functions of the class
 * defines the behaviour of the particular simulator.<br>
 * The implementations of this class are ment to be generated by
 * descendant of <code>PDUProcessorFactory</code> class whenewer
 * new connection is accepted by <code>SMSCListener</code>.
 *
 * @author Logica Mobile Networks SMPP Open Source Team
 * @version 1.0, 21 Jun 2001
 * @see PDUProcessorFactory
 * @see SMSCSession
 * @see SMSCListener
 */
public abstract class PDUProcessor
{
    /**
     * The group the processor belongs to. It's good for having overall control
     * over specific group of processors, e.g. for those generated
     * for particular listener.
     */
    private PDUProcessorGroup group = null;
    
    /**
     * If the processor is still processing, i.e. wasn't exited.
     * @see #exit()
     */
    private boolean active = true;
    
    /**
     * Private variables initialsed to default values.
     */
    public PDUProcessor()
    {
    }

    /**
     * Initialises the processor with the given group.
     * The group is basicaly intended to be a group of active
     * processors.
     * @param group the group this processor belongs to
     */
    public PDUProcessor(PDUProcessorGroup group)
    {
        setGroup(group);
    }
    
    /**
     * Meant to process <code>request</code>s received from client.
     * @param request the request received from client
     */
    public abstract void clientRequest(Request request) throws InterruptedException;
    
    /**
     * Meant to process <code>response</code>s received from client.
     * @param response the response received from client
     */
    public abstract void clientResponse(Response response);
    
    /**
     * Meant to process <code>request</code>s sent on behalf of the server.
     * This method is called by server and typically only sends the PDU
     * to the client.
     * @param request the request which has to be sent to client
     */
    public abstract void serverRequest(Request request);
    
    /**
     * Meant to process <code>response</code>s sent on behalf of the server.
     * This method is called by server and typically only sends the PDU
     * to the client.
     * @param response the response which has to be sent to client
     */
    public abstract void serverResponse(Response response);
    
    /**
     * Sets the group which the pdu processor belongs to.
     * Processor can belong to only one group.
     * @param g the new group for the processor
     */
    public void setGroup(PDUProcessorGroup g)
    {
        if (group != null) {
            group.remove(this);
        }
        group = g;
        if (group!=null) {
            group.add(this);
        }
    }
    
    /**
     * Returns the group of this pdu processor.
     * @return the current group of the processor
     * @see #setGroup(PDUProcessorGroup)
     */
    public PDUProcessorGroup getGroup() { return group; }
    
    /**
     * Returns if this pdu processor is still active.
     * @return the activity status of the processor
     * @see #exit()
     */
    public boolean isActive() { return active; }
    
    /**
     * Sets the processor to inactive state.
     * Removes the processor from the group it belonged to.
     * Called from <code>SMSCSession</code>.
     * @see SMSCSession#run()
     */
    protected void exit()
    {
        if (group != null) {
            group.remove(this);
        }
        active = false;
    }
}
