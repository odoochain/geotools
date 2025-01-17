/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.mongodb;

import java.util.Collections;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.filter.spatial.DWithinImpl;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.spatial.DWithin;

public class MongoFilterSplitterTest {

    private static final DWithinImpl D_WITHIN =
            new DWithinImpl(
                    new AttributeExpressionImpl("geometry"),
                    new LiteralExpressionImpl("POINT (5.006253 60.701807)"));
    private static final FilterCapabilities FCS = new FilterCapabilities(DWithin.class);

    @Test
    public void testDWithinSplitWithCorrectIndex() {
        MongoFilterSplitter splitter =
                new MongoFilterSplitter(
                        FCS,
                        null,
                        null,
                        new MongoCollectionMeta(Collections.singletonMap("geometry", "2dsphere")));
        splitter.visit(D_WITHIN, null);
        Assert.assertEquals(D_WITHIN, splitter.getFilterPre());
    }

    @Test
    public void testDWithinSplitIncorrectIndex() {
        MongoFilterSplitter splitter =
                new MongoFilterSplitter(
                        FCS,
                        null,
                        null,
                        new MongoCollectionMeta(Collections.singletonMap("_id", "1")));
        splitter.visit(D_WITHIN, null);
        Assert.assertEquals(D_WITHIN, splitter.getFilterPost());
    }

    @Test
    public void testDWithinSplitWithoutIndex() {
        MongoFilterSplitter splitter = new MongoFilterSplitter(FCS, null, null, null);
        splitter.visit(D_WITHIN, null);
        Assert.assertEquals(D_WITHIN, splitter.getFilterPost());
    }
}
