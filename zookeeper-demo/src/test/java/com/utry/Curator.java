package com.utry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

public class Curator {

    public static void main(String[] args) throws Exception {
        ExponentialBackoffRetry exponentialBackoffRetry = new ExponentialBackoffRetry(1000, 3);

        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("10.0.10.115:2181", exponentialBackoffRetry);

        curatorFramework.start();

        CuratorCache curatorCache = CuratorCache.builder(curatorFramework, "/curator-node").build();

//        NodeCacheListener nodeCacheListener = new NodeCacheListener() {
//            @Override
//            public void nodeChanged() throws Exception {
//                System.out.println("current node is changed");
//            }
//        };

        // 缓存数据
//        PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener() {
//            @Override
//            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent)
//                    throws Exception {
//                System.out.println("事件路径:" + pathChildrenCacheEvent.getData().getPath() + "事件类型"
//                        + pathChildrenCacheEvent.getType());
//            }
//        };
//
//        Watcher watcher = new Watcher() {
//            @Override
//            public void process(WatchedEvent event) {
//                System.out.println("this is watcher of "+ event.getPath());
//            }
//        };
//
//        curatorFramework.getData().usingWatcher(watcher).forPath("/curator-node");
//
//        curatorFramework.getZookeeperClient().getZooKeeper().getData("/curator-node",watcher,new Stat());

//        CuratorCacheListener listener = CuratorCacheListener.builder()
//                .forPathChildrenCache(curatorFramework, pathChildrenCacheListener).build();
//
//        curatorCache.listenable().addListener(listener);
//
//        curatorCache.start();

        InterProcessMutex interProcessMutex = new InterProcessMutex(curatorFramework, "/curator-node");

        interProcessMutex.acquire();

        Thread.sleep(20000000);

//        String path = curatorFramework.create().forPath("/curator-node");

//        System.out.println(path);


    }
}
