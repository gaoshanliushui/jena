/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jena.riot.adapters;

import java.io.IOException ;
import java.io.InputStream ;
import java.util.Iterator ;

import org.apache.jena.atlas.lib.IRILib ;
import org.apache.jena.atlas.web.TypedInputStream ;
import org.apache.jena.riot.stream.* ;
import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;

import com.hp.hpl.jena.rdf.model.Model ;
import com.hp.hpl.jena.shared.NotFoundException ;
import com.hp.hpl.jena.util.FileUtils ;
import com.hp.hpl.jena.util.LocationMapper ;
import com.hp.hpl.jena.util.TypedStream ;

/** FileManager
 * 
 * A FileManager provides access to named file-like resources by opening
 * InputStreams to things in the filing system, by URL (http: and file:) and
 * found by the classloader.  It can also load RDF data from such a system
 * resource into an existing model or create a new (Memory-based) model.
 * There is a global FileManager which provide uniform access to system
 * resources: applications may also create specialised FileManagers.
 * 
 * A FileManager contains a list of location functions to try: the global
 * FileManger has one {@link LocatorFile2}, one {@link LocatorClassLoader} and
 * one {@link LocatorURL2}
 * 
 * Main operations:
 * <ul>
 * <li>loadModel, readModel : URI to model</li>
 * <li>open, openNoMap : URI to input stream</li>
 * <li>mapURI : map URI to another by {@link LocationMapper}</li> 
 * </ul>
 * 
 * Utilities:
 * <ul>
 * <li>readWholeFileAsUTF8</li>
 * </ul>
 * 
 * A FileManager works in conjunction with a LocationMapper.
 * A {@link LocationMapper} is a set of alternative locations for system
 * resources and a set of alternative prefix locations.  For example, a local
 * copy of a common RDF dataset may be used whenever the usual URL is used by
 * the application.
 *
 * The {@link LocatorFile2} also supports the idea of "current directory".
 * 
 * @see StreamManager
 * @see LocationMapper
 * @see FileUtils
 */
 
public class AdapterFileManager extends com.hp.hpl.jena.util.FileManager
{
    // This is a legacy class -  it provides  FileManager calls onto the RIOT equivalents.  
    // The different aspects are now split out 
    // and this class maintains the old interface. 
    // Aspects: StreamManager, LocationMapper, Model cache.
    // and this class exists to maintain the old interfaces and the
    // combination of stream and model reading.
    
    // RIOT reader uses StreamManager.
    // Each FileManager has a StreamManager.
    
    /** Delimiter between path entries : because URI scheme names use : we only allow ; */
    //public static final String PATH_DELIMITER = ";";
    //public static final String filePathSeparator = java.io.File.separator ;
    
    private static Logger log = LoggerFactory.getLogger(AdapterFileManager.class) ;

    private static AdapterFileManager instance = null ;
    private final StreamManager streamManager ;
    
    // -------- Cache operations
    // See also loadModelWorker which uses the cache.
    // These are in the FileManager for legacy reasons.
    private FileManagerModelCache modelCache = new FileManagerModelCache() ;

    /** Get the global file manager.
     * @return the global file manager
     */
    public static AdapterFileManager get()
    {
        if ( instance == null )
            instance = makeGlobal() ;
        return instance ;
    }
    
    /** Set the global file manager (as returned by get())
     * If called before any call to get(), then the usual default filemanager is not created 
     * @param globalFileManager
     */
    public static void setGlobalFileManager(AdapterFileManager globalFileManager)
    {
        instance = globalFileManager ;
    }
    
    /** Create an uninitialized FileManager */
    private AdapterFileManager()
    { 
        streamManager = new StreamManager() ;
    }
    
    /** Create a new file manager that is a copy of another.
     * Location mapper and locators chain are copied (the locators are not cloned).
     * @param filemanager
     */
    public AdapterFileManager(com.hp.hpl.jena.util.FileManager filemanager)
    {
        this() ;
        Iterator<com.hp.hpl.jena.util.Locator> iter = filemanager.locators() ;
        while ( iter.hasNext() )
            streamManager.addLocator(AdapterLib.convert(iter.next())) ;

        LocationMapper locmap = filemanager.getLocationMapper() ;
        streamManager.setLocationMapper(AdapterLib.copyConvert(locmap)) ;
    }

    public AdapterFileManager(StreamManager streamManager)
    {
        this(streamManager, (LocationMapper2)null) ;
    }
    
    /** Create a FileManger using a RIOT StreamManager and RIOT LocationMapper  */
    public AdapterFileManager(StreamManager streamManager, LocationMapper2 mapper)
    { 
        if ( streamManager == null )
            streamManager = new StreamManager() ;
        this.streamManager = streamManager ; 
        streamManager.setLocationMapper(mapper) ;
    }
    
    /** Create a "standard" FileManager. */
    public static AdapterFileManager makeGlobal()
    {
        AdapterFileManager fMgr = new AdapterFileManager(StreamManager.get(), (LocationMapper2)null) ;
        return fMgr ;
    }
    
    /** Return the associate stream manager */ 
    public StreamManager getStreamManager()         { return streamManager ; } 
    
    /** Set the location mapping */
    @Override
    public void setLocationMapper(LocationMapper mapper)   { streamManager.setLocationMapper(AdapterLib.copyConvert(mapper)) ; }
    
    /** Get the location mapping */
    @Override
    public LocationMapper getLocationMapper()               { return new AdapterLocationMapper(streamManager.getLocationMapper()) ; }
    
    /** Return an iterator over all the handlers */
    @Override
    public Iterator<com.hp.hpl.jena.util.Locator> locators()    { throw new UnsupportedOperationException() ; }

    /** Remove a locator */ 
    @Override
    public void remove(com.hp.hpl.jena.util.Locator loc)                             { throw new UnsupportedOperationException() ; }

    /** Add a locator to the end of the locators list */ 
    @Override
    public void addLocator(com.hp.hpl.jena.util.Locator oldloc)
    {
        Locator loc = AdapterLib.convert(oldloc) ;
        log.debug("Add location: "+loc.getName()) ;
        streamManager.addLocator(loc) ; }

    /** Add a file locator */
    @Override
    public void addLocatorFile() { addLocatorFile(null) ; } 

    /** Add a file locator which uses dir as its working directory */ 
    @Override
    public void addLocatorFile(String dir)
    {
        LocatorFile2 fLoc = new LocatorFile2(dir) ;
        streamManager.addLocator(fLoc) ;
    }
    
    /** Add a class loader locator */ 
    @Override
    public void addLocatorClassLoader(ClassLoader cLoad)
    {
        LocatorClassLoader cLoc = new LocatorClassLoader(cLoad) ;
        streamManager.addLocator(cLoc) ;
    }

    /** Add a URL locator */
    @Override
    public void addLocatorURL()
    {
        Locator loc = new LocatorURL2() ;
        streamManager.addLocator(loc) ;
    }

    /** Add a zip file locator */
    @Override
    public void addLocatorZip(String zfn)
    {
        Locator loc = new LocatorZip(zfn) ;
        streamManager.addLocator(loc) ;
    }
    
    // -------- Cache operations (start)
    /** Reset the model cache */
    @Override
    public void resetCache()                        { modelCache.resetCache() ; }

    /** Change the state of model cache : does not clear the cache */ 
    @Override
    public void setModelCaching(boolean state)      { modelCache.setModelCaching(state) ; }
    
    /** return whether caching is on of off */
    @Override
    public boolean getCachingModels()               { return modelCache.getCachingModels() ; }
    
    /** Read out of the cache - return null if not in the cache */ 
    @Override
    public Model getFromCache(String filenameOrURI) { return modelCache.getFromCache(filenameOrURI) ; }

    @Override
    public boolean hasCachedModel(String filenameOrURI)
    { return modelCache.hasCachedModel(filenameOrURI) ; } 
    
    @Override
    public void addCacheModel(String uri, Model m)  { modelCache.addCacheModel(uri, m) ; }

    @Override
    public void removeCacheModel(String uri)        { modelCache.removeCacheModel(uri) ; }
    // -------- Cache operations (end)

    @Override
    protected Model readModelWorker(Model model, String filenameOrURI, String baseURI, String syntax)
    {
        // Doesn't call open() - we want to make the synatx guess based on the mapped URI.
        String mappedURI = mapURI(filenameOrURI) ;

        if ( log.isDebugEnabled() && ! mappedURI.equals(filenameOrURI) )
            log.debug("Map: "+filenameOrURI+" => "+mappedURI) ;

        if ( syntax == null && baseURI == null && mappedURI.startsWith( "http:" ) )
        {
            // No syntax, no baseURI, HTTP URL ==> use content negotiation
            model.read(mappedURI) ;
            return model ;
        }
        
        if ( syntax == null )
        {
            syntax = FileUtils.guessLang(mappedURI) ;
            if ( syntax == null || syntax.equals("") )
                syntax = FileUtils.langXML ;
            if ( log.isDebugEnabled() ) 
                log.debug("Syntax guess: "+syntax);
        }

        if ( baseURI == null )
            baseURI = chooseBaseURI(filenameOrURI) ;

        TypedInputStream in = streamManager.openNoMapOrNull(mappedURI) ;
        if ( in == null )
        {
            if ( log.isDebugEnabled() )
                log.debug("Failed to locate '"+mappedURI+"'") ;
            throw new NotFoundException("Not found: "+filenameOrURI) ;
        }
        if ( in.getMediaType() != null )
        {
            // XXX
            //syntax
        }
        model.read(in, baseURI, syntax) ;
        try { in.close(); } catch (IOException ex) {}
        return model ;
    }

    private static String chooseBaseURI(String baseURI)
    {
        // Use IRILib.filenameToIRI
        String scheme = FileUtils.getScheme(baseURI) ;
        
        if ( scheme != null && ! scheme.equals("file") )
            // Not file: - leave alone.
            return baseURI ;
        
        return IRILib.filenameToIRI(baseURI) ;
    }
    
    /** Open a file using the locators of this FileManager 
     *  Throws RiotNotFoundException if not found.*/ 
    @Override
    public InputStream open(String filenameOrURI)
    { 
        return streamManager.open(filenameOrURI) ;
    }
    
    /** Apply the mapping of a filename or URI */
    @Override
    public String mapURI(String filenameOrURI)                  { return streamManager.mapURI(filenameOrURI) ; }
    
    /** Open a file using the locators of this FileManager 
     *  but without location mapping.  Throws RiotNotFoundException if not found.*/ 
    @Override
    public InputStream openNoMap(String filenameOrURI)          { return streamManager.openNoMap(filenameOrURI) ; }

    /** Open a file using the locators of this FileManager 
     *  without location mapping. Return null if not found
     */ 
    @Override
    public TypedStream openNoMapOrNull(String filenameOrURI)    { return AdapterLib.convert(streamManager.openNoMapOrNull(filenameOrURI)) ; }
    
    /** @deprecated Use mapURI */
    @Deprecated
    @Override
    public String remap(String filenameOrURI)
    { return mapURI(filenameOrURI) ; }
}