package nodomain.freeyourgadget.gadgetbridge.devices.huawei;

import android.content.Context;
import android.net.Uri;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nodomain.freeyourgadget.gadgetbridge.R;
import nodomain.freeyourgadget.gadgetbridge.activities.InstallActivity;
import nodomain.freeyourgadget.gadgetbridge.devices.DeviceCoordinator;
import nodomain.freeyourgadget.gadgetbridge.devices.InstallHandler;

import nodomain.freeyourgadget.gadgetbridge.impl.GBDevice;
import nodomain.freeyourgadget.gadgetbridge.model.GenericItem;
import nodomain.freeyourgadget.gadgetbridge.service.devices.huawei.HuaweiFwHelper;
import nodomain.freeyourgadget.gadgetbridge.service.devices.huawei.HuaweiWatchfaceManager;

public class HuaweiInstallHandler implements InstallHandler {
    private static final Logger LOG = LoggerFactory.getLogger(HuaweiInstallHandler.class);

    private final Context context;
    protected final HuaweiFwHelper helper;

    boolean valid = false;

    public HuaweiInstallHandler(Uri uri, Context context) {
        this.context = context;
        this.helper = new HuaweiFwHelper(uri, context);
    }


    @Override
    public void validateInstallation(InstallActivity installActivity, GBDevice device) {

        final DeviceCoordinator coordinator = device.getDeviceCoordinator();
        if (!(coordinator instanceof HuaweiCoordinatorSupplier)) {
            LOG.warn("Coordinator is not a HuaweiCoordinatorSupplier: {}", coordinator.getClass());
            installActivity.setInstallEnabled(false);
            return;
        }

        final HuaweiCoordinatorSupplier huaweiCoordinatorSupplier = (HuaweiCoordinatorSupplier) coordinator;

        HuaweiWatchfaceManager.WatchfaceDescription description = helper.getWatchfaceDescription();

        HuaweiWatchfaceManager.Resolution resolution = new HuaweiWatchfaceManager.Resolution();
        String deviceScreen =  String.format("%d*%d",huaweiCoordinatorSupplier.getHuaweiCoordinator().getHeight(),
                huaweiCoordinatorSupplier.getHuaweiCoordinator().getWidth());
        this.valid = resolution.isValid(description.screen, deviceScreen);

        installActivity.setInstallEnabled(true);

        GenericItem installItem = new GenericItem();


        if (helper.getWatchfacePreviewBitmap() != null) {
            installItem.setPreview(helper.getWatchfacePreviewBitmap());
        }

        installItem.setName(description.title);
        installActivity.setInstallItem(installItem);
        if (device.isBusy()) {
            LOG.error("Firmware cannot be installed (device busy)");
            installActivity.setInfoText("Firmware cannot be installed (device busy)");
            installActivity.setInfoText(device.getBusyTask());
            installActivity.setInstallEnabled(false);
            return;
        }

        if ( !device.isConnected()) {
            LOG.error("Firmware cannot be installed (not connected or wrong device)");
            installActivity.setInfoText("Firmware cannot be installed (not connected or wrong device)");
            installActivity.setInstallEnabled(false);
            return;
        }

        if (!this.valid) {
            LOG.error("Watchface cannot be installed");
            installActivity.setInfoText("Watchface resolution doesnt match device screen. Watchface is "
                    + resolution.screenByThemeVersion(description.screen) + " device screen is " + deviceScreen);
            installActivity.setInstallEnabled(false);
            return;
        }


        //installItem.setDetails(description.version);

        installItem.setIcon(R.drawable.ic_watchface);
        installActivity.setInfoText(context.getString(R.string.watchface_install_info, installItem.getName(), description.version, description.author));

        LOG.debug("Initialized HuaweiInstallHandler");

    }

    @Override
    public boolean isValid() {
        return helper.isValid();
    }

    @Override
    public void onStartInstall(GBDevice device) {
        helper.unsetFwBytes();
    }
}
