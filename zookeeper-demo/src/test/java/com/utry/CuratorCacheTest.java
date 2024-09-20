package com.utry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.ThreadLocalRandom;

public class CuratorCacheTest {

    public static void main(String[] args) throws Exception {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("10.0.10.115:2181,10.0.10.114:2181,10.0.10.113:2181", new ExponentialBackoffRetry(1000, 3));

        curatorFramework.start();

        CuratorCache cache = CuratorCache.build(curatorFramework, "/curator-node");

        CuratorCacheListener listener = CuratorCacheListener.builder()
                .forCreates(node -> System.out.println(String.format("Node created: [s%]",node)))
                .forChanges((oldNode, node) -> System.out.println(String.format("Node changed. Old: [%s] New: [%s]", oldNode, node)))
                .forDeletes(oldNode -> System.out.println(String.format("Node deleted. Old value: [%s]", oldNode)))
                .forInitialized(() -> System.out.println("Cache initialized"))
                .build();

        // register the listener
        cache.listenable().addListener(listener);

        cache.start();

        // now randomly create/change/delete nodes
        for ( int i = 0; i < 1000; ++i )
        {
            int depth = random.nextInt(1, 4);
            String path = makeRandomPath(random, depth);
            if ( random.nextBoolean() )
            {
                curatorFramework.create().orSetData().creatingParentsIfNeeded().forPath(path, Long.toString(random.nextLong()).getBytes());
            }
            else
            {
                curatorFramework.delete().quietly().deletingChildrenIfNeeded().forPath(path);
            }

            Thread.sleep(5);
        }
    }

    private static String makeRandomPath(ThreadLocalRandom random, int depth)
    {
        if ( depth == 0 )
        {
            return "/curator-node";
        }
        return makeRandomPath(random, depth - 1) + "/" + random.nextInt(3);
    }
}
