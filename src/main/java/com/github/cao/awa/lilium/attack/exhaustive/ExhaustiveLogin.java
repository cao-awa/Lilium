package com.github.cao.awa.lilium.attack.exhaustive;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.lilium.network.router.request.RequestRouter;

import java.util.List;

public class ExhaustiveLogin {
    private static final List<RequestRouter> invalidRouters = ApricotCollectionFactor.timedList(1500);

    public static boolean validate(RequestRouter router) {
        if (invalidRouters.contains(router)) {
            return false;
        } else {
            invalidRouters.add(router);
            return true;
        }
    }
}
