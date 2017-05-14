package com.minorius.blitz;

import com.minorius.blitz.db.DbSize;
import com.minorius.blitz.db.OutputObject;
import com.minorius.blitz.model.TransferableData;
import com.minorius.blitz.observ.ObservableAnswers;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ChannelFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.Channels;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.logging.Handler;

public class Main {

    public static Map<Integer, ObservableAnswers> sessionsOfGame;
    public static List<Socket> poolWithSockets;
    public static int numberOfQuestionsFromDb;

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException, InterruptedException {


        sessionsOfGame = new HashMap<>();
        poolWithSockets = new ArrayList<>();

        DbSize dbSize = new DbSize();
        numberOfQuestionsFromDb = dbSize.getNumberOfQuestionsFromDb();

        PortListener.portListenerOn();

    }
}
