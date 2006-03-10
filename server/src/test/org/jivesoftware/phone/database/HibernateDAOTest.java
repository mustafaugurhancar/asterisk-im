/**
 * $RCSfile: HibernateDAOTest.java,v $
 * $Revision: 1.8 $
 * $Date: 2005/06/24 19:32:50 $
 *
 * Copyright (C) 1999-2004 Jive Software. All rights reserved.
 *
 * This software is the proprietary information of Jive Software. Use is subject to license terms.
 */
package org.jivesoftware.phone.database;

import org.jivesoftware.phone.PhoneDevice;
import org.jivesoftware.phone.PhoneUser;
import org.jivesoftware.phone.util.ThreadPool;
import junit.framework.TestCase;
import org.jivesoftware.util.JiveGlobals;

import java.util.Collection;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Andrew Wright
 */
public class HibernateDAOTest extends TestCase {

    static {
        JiveGlobals.setConfigName("wildfire.xml");
        JiveGlobals.getPropertyNames(); // just called to intialize jive globals
        ThreadPool.init();


    }

    public void testCRUD() throws Exception {

        Logger log = Logger.getLogger("org.hibernate.SQL");
        log.setLevel(Level.ALL);

        ConsoleHandler h = new ConsoleHandler();
        h.setLevel(Level.ALL);
        log.addHandler(h);

        PhoneDAO phoneDAO = new HibernatePhoneDAO();


        PhoneUser phoneJID = new PhoneUser("andrew");
        PhoneDevice device = new PhoneDevice("SIP/1231");
        device.setPrimary(true);
        phoneJID.addDevice(device);
        phoneDAO.save(phoneJID);

        assertTrue(phoneJID.getId() > 0);

        PhoneUser phoneJID2 = phoneDAO.getByID(phoneJID.getId());
        assertNotNull(phoneJID2);
        assertEquals(phoneJID, phoneJID2);

        PhoneDevice primary = phoneJID.getPrimaryDevice();
        assertNotNull(primary);

        phoneJID2 = phoneDAO.getByDevice(device.getDevice());
        assertNotNull(phoneJID);
        assertEquals(phoneJID, phoneJID2);


        Collection<PhoneUser> phones = phoneDAO.getALL();
        for (PhoneUser pjid : phones) {
            assertNotNull(pjid);
        }

        phoneJID2 = phoneDAO.getByUsername("andrew");
        assertNotNull(phoneJID);
        assertEquals(phoneJID, phoneJID2);


        phoneDAO.remove(phoneJID);

        phoneJID = phoneDAO.getByID(phoneJID.getId());

        assertNull(phoneJID);


    }

    public void testGetByDevice() {
        // assumes the device is in the database


        PhoneDAO phoneDAO = new HibernatePhoneDAO();

        PhoneUser phoneJID = phoneDAO.getByDevice("SIP/6131");

        assertNotNull(phoneJID);


    }


}