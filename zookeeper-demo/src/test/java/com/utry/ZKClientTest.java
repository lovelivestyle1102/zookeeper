package com.utry;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Before;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZKClientTest {
    private static final String ZK_ADDRESS="10.0.10.115:2181";

    private static final int SESSION_TIMEOUT = 5000;

    private static ZooKeeper zooKeeper;

    private static final String ZK_NODE="/zk-node";


    @Before
    public void init() throws IOException, InterruptedException {
        final CountDownLatch countDownLatch=new CountDownLatch(1);
        zooKeeper=new ZooKeeper(ZK_ADDRESS, SESSION_TIMEOUT, event -> {
            if (event.getState()== Watcher.Event.KeeperState.SyncConnected &&
                    event.getType()== Watcher.Event.EventType.None){
                countDownLatch.countDown();
                System.out.println("连接成功！");
            }
        });
        System.out.println("连接中....");
        countDownLatch.await();
//        zooKeeper.create();
        zooKeeper.getData()
    }
}
