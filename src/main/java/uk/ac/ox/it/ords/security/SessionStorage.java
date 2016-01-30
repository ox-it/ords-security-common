/*
 * Copyright 2015 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ox.it.ords.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * A very basic session store that stores sessions on disk.
 * 
 * Suitable for a few scenarios where enterprise session caching is not available;
 * you should use ehcache or similar instead of this class wherever possible
 */
public class SessionStorage {
	
    protected final String storeName;
    
    private final File diskFile;

    SessionStorage(final String name) {
    	
        storeName = name;
        diskFile = new File(System.getProperty("java.io.tmpdir"), name);
        
        System.out.println("####Session storage is at:"+diskFile.getAbsolutePath());

    }

    void initStore(Serializable itemToStore){
        if (!diskFile.exists()) {
            store(itemToStore);
        }
    }

    void store(final Serializable itemToStore){
        try {

            final FileOutputStream fos = new FileOutputStream(diskFile);
            try {
                final ObjectOutputStream os = new ObjectOutputStream(fos);
                try {
                    os.writeObject(itemToStore);
                } finally {
                    os.close();
                }
            } finally {
                fos.close();
            }

        } catch (IOException e) {
            throw new RuntimeException("Could not save object to disk.", e);
        }
    }
    
    Object load(){
        try {
            final FileInputStream fis = new FileInputStream(diskFile);
            try {
                final ObjectInputStream is = new ObjectInputStream(fis);
                try {
                    try {
                        //noinspection unchecked
                        return is.readObject();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                } finally {
                    is.close();
                }
            } finally {
                fis.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
