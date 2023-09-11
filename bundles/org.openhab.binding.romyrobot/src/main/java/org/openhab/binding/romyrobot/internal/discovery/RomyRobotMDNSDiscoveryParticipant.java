/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.romyrobot.internal.discovery;

import static org.openhab.binding.romyrobot.internal.RomyRobotBindingConstants.*;
import static org.openhab.core.thing.Thing.*;

import java.util.Collections;
import java.util.Set;

import javax.jmdns.ServiceInfo;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.romyrobot.internal.api.RomyApiFactory;
import org.openhab.core.config.discovery.DiscoveryResult;
import org.openhab.core.config.discovery.DiscoveryResultBuilder;
import org.openhab.core.config.discovery.mdns.MDNSDiscoveryParticipant;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.ThingUID;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a discovery participant which finds RomyRobots on the local network
 * through their mDNS announcements.
 *
 * @author Manuel Dipolt - Initial contribution
 *
 */
@NonNullByDefault
@Component
public class RomyRobotMDNSDiscoveryParticipant implements MDNSDiscoveryParticipant {

    private final Logger logger = LoggerFactory.getLogger(RomyRobotMDNSDiscoveryParticipant.class);

    private RomyApiFactory apiFactory;

    @Activate
    public RomyRobotMDNSDiscoveryParticipant(
            @Reference RomyApiFactory apiFactory /* @Reference HttpClientFactory httpClientFactory */) {
        logger.debug("Activating ROMY Discovery service");
        this.apiFactory = apiFactory;
    }

    @Override
    public String getServiceType() {
        return "_aicu-http._tcp.local.";
    }

    @Override
    public Set<ThingTypeUID> getSupportedThingTypeUIDs() {
        return Collections.emptySet();
    }

    @Override
    public @Nullable ThingUID getThingUID(ServiceInfo service) {
        return new ThingUID(THING_TYPE_ROMY, "");
    }

    @Override
    public @Nullable DiscoveryResult createResult(ServiceInfo service) {

        final ThingUID uid = getThingUID(service);
        if (uid == null) {
            logger.error("uid is null!");
            return null;
        }
        logger.info("Discovered ROMY vacuum cleaner robot: {}", service);

        // get IP address
        String address = "";
        String robotname = "";
        String[] hostAddresses = service.getHostAddresses();

        if ((hostAddresses == null) || (hostAddresses.length > 0)) {
            logger.error("ROMY discovered empty IP via MDNS!");
            return null;
        }
        logger.info("hostAddresses: {}", hostAddresses);

        address = hostAddresses[0];
        if (address.isEmpty()) {
            logger.error("ROMY discovered empty IP via MDNS!");
            return null;
        }
        logger.info("address: {}", address);

        /*
         * try {
         * RomyRobotConfiguration config = new RomyRobotConfiguration();
         * config.hostname = address;
         * RomyApi romyDevice = apiFactory.getHttpApi(config);
         * romyDevice.refresh();
         * robotname = romyDevice.getName();
         * logger.error("new ROMY discovered: {}", robotname);
         * } catch (Exception e) {
         * e.printStackTrace();
         * logger.error("Error setting up ROMY api: {}", e.getMessage());
         * return null;
         * }
         */

        return DiscoveryResultBuilder.create(uid).withProperty(PROPERTY_SERIAL_NUMBER, service.getName())
                .withLabel("robotname").withRepresentationProperty(PROPERTY_SERIAL_NUMBER).build();
    }
}
