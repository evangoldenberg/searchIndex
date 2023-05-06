package org.ezcampus.search.rest;

import java.lang.Thread.UncaughtExceptionHandler;

import org.ezcampus.search.System.GlobalSettings;
import org.ezcampus.search.System.ResourceLoader;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.tinylog.Logger;

import jakarta.ws.rs.ApplicationPath;

// MAIN ENTRY TO JAVA API PROGRAM

@ApplicationPath("/")
public class Program extends ResourceConfig {

	private static class TinylogHandler implements UncaughtExceptionHandler 
    {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) 
        {
            Logger.error("! === Unhandled Exception === !");
            Logger.error(ex);
        }
    }
	
    public Program() {
        GlobalSettings.IS_DEBUG = true;

        ResourceLoader.loadTinyLogConfig();

        Logger.info("{} starting...", GlobalSettings.BRAND_LONG);
        Logger.info("Running as debug: {}", GlobalSettings.IS_DEBUG);

        // Set the package where the resources are located
        packages("org.ezcampus.search.rest");


    }
}
