package de.spiritaner.maz.controller.meta;

import de.spiritaner.maz.model.meta.Religion;

/**
 * Created by florian on 2/26/17.
 */
public class ReligionOverviewController extends MetadataOverviewController<Religion> {

    public ReligionOverviewController() {
        super(Religion.class);
    }

    @Override
    // TODO Extract strings
    public String getMetaName() {
        return "Religion";
    }
}
