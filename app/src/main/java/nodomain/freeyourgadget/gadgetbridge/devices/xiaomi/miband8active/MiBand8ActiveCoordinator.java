/*  Copyright (C) 2024 José Rebelo

    This file is part of Gadgetbridge.

    Gadgetbridge is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Gadgetbridge is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>. */
package nodomain.freeyourgadget.gadgetbridge.devices.xiaomi.miband8active;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.Nullable;

import java.util.regex.Pattern;

import nodomain.freeyourgadget.gadgetbridge.R;
import nodomain.freeyourgadget.gadgetbridge.devices.InstallHandler;
import nodomain.freeyourgadget.gadgetbridge.devices.xiaomi.XiaomiCoordinator;
import nodomain.freeyourgadget.gadgetbridge.devices.xiaomi.XiaomiInstallHandler;

public class MiBand8ActiveCoordinator extends XiaomiCoordinator {
    @Override
    public boolean isExperimental() {
        return true;
    }

    @Override
    protected Pattern getSupportedDeviceName() {
        return Pattern.compile("^Xiaomi( Smart)? Band 8 Active [A-Z0-9]{4}$");
    }

    @Nullable
    @Override
    public InstallHandler findInstallHandler(final Uri uri, final Context context) {
        final XiaomiInstallHandler handler = new XiaomiInstallHandler(uri, context);
        return handler.isValid() ? handler : null;
    }

    @Override
    public int getDeviceNameResource() {
        return R.string.devicetype_miband8active;
    }

    @Override
    public int getDefaultIconResource() {
        return R.drawable.ic_device_default;
    }

    @Override
    public int getDisabledIconResource() {
        return R.drawable.ic_device_default_disabled;
    }

    @Override
    public boolean supportsActivityDataFetching() {
        // FIXME still has some issues
        return true;
    }

    @Override
    public boolean supportsActivityTracking() {
        // FIXME still has some issues
        return true;
    }

    @Override
    public boolean supportsActivityTracks() {
        // FIXME still has some issues
        return true;
    }
}
