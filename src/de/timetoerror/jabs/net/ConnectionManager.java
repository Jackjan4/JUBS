/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.timetoerror.jabs.net;

import de.janroslan.jputils.logging.LoggerUtils;
import de.timetoerror.jabs.Environment;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jackjan
 */
public final class ConnectionManager {

    // Logger
    private static final Logger LOGGER
            = Logger.getLogger("ConnectionManager");

    private Selector selector;
    private boolean running;
    private final ExecutorService executor;
    private final int port;

    public ConnectionManager(int port) {
        this.port = port;
        
        LoggerUtils.registerLoggerToFile(LOGGER, Environment.getInstance().getLogPath() + File.separator + "connections.log", true, true);
        
        executor = Executors.newCachedThreadPool();
        
        // Init Selector
        try {
            selector = Selector.open();
        } catch (IOException ex) {
            
        }
    }

    /**
     * Starts the proccess of selecting ready channels for reading
     */
    public void run() {
        try {
            running = true;

            // Init ServerSocket, run it and register it in selector
            ServerSocketChannel serversocket = initServerSocket(port);
            serversocket.register(selector, SelectionKey.OP_ACCEPT);

            // The connection listener loop
            while (running) {
                try {
                    if (selector.select() > 0) {
                        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                        while (iterator.hasNext()) {
                            SelectionKey key = iterator.next();

                            // ServerSocket event
                            if (key.isAcceptable()) {
                                ServerSocketChannel chan = (ServerSocketChannel) key.channel();
                                SocketChannel child = chan.accept();

                                // Log incoming connection
                                LOGGER.log(Level.INFO, "Connection from {0} is incoming", child.getRemoteAddress());

                                child.configureBlocking(false);
                                child.register(selector, SelectionKey.OP_READ);
                                // Client Socket event
                            } else if (key.isReadable()) {
                                SocketChannel chan = (SocketChannel) key.channel();

                                // Remove connection from Selector
                                key.cancel();

                                // Log incoming message
                                LOGGER.log(Level.INFO, "Client {0} send a message",chan.getRemoteAddress());

                                // Process message
                                executor.submit(new MessageProcessor(chan));
                            }

                            // Delete selector request
                            iterator.remove();
                        }
                    }
                } catch (IOException ex) {
                    System.out.println("Error: ");
                }

            }
        } catch (ClosedChannelException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Inits and starts the ServerSocket
     *
     * @param port
     * @return
     */
    private ServerSocketChannel initServerSocket(int port) {
        ServerSocketChannel serversocket = null;
        try {
            serversocket = ServerSocketChannel.open();
            serversocket.bind(new InetSocketAddress(port));
            serversocket.configureBlocking(false);

        } catch (IOException ex) {
            System.out.println("Error: ");
            ex.printStackTrace();
        }

        return serversocket;
    }

    /**
     * Stops the listening for connections
     */
    public void stop() {
        running = false;
    }

}
