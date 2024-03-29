/*
 * Copyright (c) 2009-2012 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.network.kernel.udp;

import com.jme3.network.kernel.Endpoint;
import com.jme3.network.kernel.Kernel;
import com.jme3.network.kernel.KernelException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;


/**
 *  Endpoint implementation that encapsulates the
 *  UDP connection information for return messaging,
 *  identification of envelope sources, etc.
 *
 *  @version   $Revision: 9856 $
 *  @author    Paul Speed
 */
public class UdpEndpoint implements Endpoint
{
    private long id;    
    private SocketAddress address;
    private DatagramSocket socket;
    private UdpKernel kernel;
    private boolean connected = true; // it's connectionless but we track logical state

    public UdpEndpoint( UdpKernel kernel, long id, SocketAddress address, DatagramSocket socket )
    {
        this.id = id;
        this.address = address;
        this.socket = socket;
        this.kernel = kernel;
    }

    public Kernel getKernel()
    {
        return kernel;
    }

    protected SocketAddress getRemoteAddress()
    {
        return address;
    }

    public void close()
    {
        close( false );
    }

    public void close( boolean flush )
    {
        // No real reason to flush UDP traffic yet... especially
        // when considering that the outbound UDP isn't even
        // queued.
    
        try {
            kernel.closeEndpoint(this);
            connected = false;
        } catch( IOException e ) {
            throw new KernelException( "Error closing endpoint for socket:" + socket, e );
        }
    }

    public long getId()
    {
        return id;
    }

    public String getAddress()
    {
        return String.valueOf(address); 
    }     

    public boolean isConnected()
    {
        // The socket is always unconnected anyway so we track our
        // own logical state for the kernel's benefit.
        return connected;
    }

    public void send( ByteBuffer data )
    {
        if( !isConnected() ) {
            throw new KernelException( "Endpoint is not connected:" + this );
        }
        
        
        /*try {
            DatagramPacket p = new DatagramPacket( data.array(), data.position(), 
                                                   data.remaining(), address );
                                                   
            // Just queue it up for the kernel threads to write
            // out
            kernel.enqueueWrite( this, p );
                                                               
            //socket.send(p);
        } catch( IOException e ) {
            throw new KernelException( "Error sending datagram to:" + address, e );
        }*/
    }

    public String toString()
    {
        return "UdpEndpoint[" + id + ", " + address + "]";
    }
}
