package stock.awesome.instock.misc_classes;

import stock.awesome.instock.misc_classes.Kit;

// Stores the kit that needs to be passed in between ViewAllKitsActivity and ViewKitDetailsActivity
// Bad software engineering practice, but implementing parcelables is time-consuming
public class KitStorer {
    public static Kit kit = null;

    public static void storeKit(Kit inKit) {
        kit = inKit;
    }

}