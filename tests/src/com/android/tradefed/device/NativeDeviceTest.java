/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.tradefed.device;

import com.android.ddmlib.IDevice;
import com.android.tradefed.util.FileUtil;
import com.android.tradefed.util.IRunUtil;

import junit.framework.TestCase;

import org.easymock.EasyMock;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Unit tests for {@link NativeDevice}.
 */
public class NativeDeviceTest extends TestCase {

    private static final String MOCK_DEVICE_SERIAL = "serial";
    private static final String FAKE_NETWORK_SSID = "FakeNet";
    private static final String FAKE_NETWORK_PASSWORD ="FakePass";

    private IDevice mMockIDevice;
    private TestableAndroidNativeDevice mTestDevice;
    private IDeviceRecovery mMockRecovery;
    private IDeviceStateMonitor mMockStateMonitor;
    private IRunUtil mMockRunUtil;
    private IWifiHelper mMockWifi;
    private IDeviceMonitor mMockDvcMonitor;

    /**
     * A {@link TestDevice} that is suitable for running tests against
     */
    private class TestableAndroidNativeDevice extends NativeDevice {
        public TestableAndroidNativeDevice() {
            super(mMockIDevice, mMockStateMonitor, mMockDvcMonitor);
        }

        @Override
        public void postBootSetup() {
            // too annoying to mock out postBootSetup actions everyone, so do nothing
        }

        @Override
        protected IRunUtil getRunUtil() {
            return mMockRunUtil;
        }

        @Override
        void doReboot() throws DeviceNotAvailableException, UnsupportedOperationException {
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMockIDevice = EasyMock.createMock(IDevice.class);
        EasyMock.expect(mMockIDevice.getSerialNumber()).andReturn(MOCK_DEVICE_SERIAL).anyTimes();
        mMockRecovery = EasyMock.createMock(IDeviceRecovery.class);
        mMockStateMonitor = EasyMock.createMock(IDeviceStateMonitor.class);
        mMockDvcMonitor = EasyMock.createMock(IDeviceMonitor.class);
        mMockRunUtil = EasyMock.createMock(IRunUtil.class);
        mMockWifi = EasyMock.createMock(IWifiHelper.class);

        // A TestDevice with a no-op recoverDevice() implementation
        mTestDevice = new TestableAndroidNativeDevice() {
            @Override
            public void recoverDevice() throws DeviceNotAvailableException {
                // ignore
            }

            @Override
            public IDevice getIDevice() {
                return mMockIDevice;
            }

            @Override
            IWifiHelper createWifiHelper() {
                return mMockWifi;
            }
        };
        mTestDevice.setRecovery(mMockRecovery);
        mTestDevice.setCommandTimeout(100);
        mTestDevice.setLogStartDelay(-1);
    }

    /**
     * Test return exception for package installation
     * {@link NativeDevice#installPackage(File, boolean, String...)}.
     */
    public void testInstallPackages_exception() {
        try {
            mTestDevice.installPackage(new File(""), false);
        } catch (UnsupportedOperationException onse) {
            return;
        } catch (DeviceNotAvailableException e) {
            fail("installPackage should have thrown an Unsupported exception, not dnae");
        }
        fail("installPackage should have thrown an exception");
    }

    /**
     * Test return exception for package installation
     * {@link NativeDevice#uninstallPackage(String)}.
     */
    public void testUninstallPackages_exception() {
        try {
            mTestDevice.uninstallPackage("");
        } catch (UnsupportedOperationException onse) {
            return;
        } catch (DeviceNotAvailableException e) {
            fail("uninstallPackage should have thrown an Unsupported exception, not dnae");
        }
        fail("uninstallPackageForUser should have thrown an exception");
    }

    /**
     * Test return exception for package installation
     * {@link NativeDevice#installPackage(File, boolean, boolean, String...)}.
     */
    public void testInstallPackagesBool_exception() {
        try {
            mTestDevice.installPackage(new File(""), false, false);
        } catch (UnsupportedOperationException onse) {
            return;
        } catch (DeviceNotAvailableException e) {
            fail("installPackage should have thrown an Unsupported exception, not dnae");
        }
        fail("installPackage should have thrown an exception");
    }

    /**
     * Test return exception for package installation
     * {@link NativeDevice#installPackageForUser(File, boolean, int, String...)}.
     */
    public void testInstallPackagesForUser_exception() {
        try {
            mTestDevice.installPackageForUser(new File(""), false, 0);
        } catch (UnsupportedOperationException onse) {
            return;
        } catch (DeviceNotAvailableException e) {
            fail("installPackageForUser should have thrown an Unsupported exception, not dnae");
        }
        fail("installPackageForUser should have thrown an exception");
    }

    /**
     * Test return exception for package installation
     * {@link NativeDevice#installPackageForUser(File, boolean, boolean, int, String...)}.
     */
    public void testInstallPackagesForUserWithPermission_exception() {
        try {
            mTestDevice.installPackageForUser(new File(""), false, false, 0);
        } catch (UnsupportedOperationException onse) {
            return;
        } catch (DeviceNotAvailableException e) {
            fail("installPackageForUser should have thrown an Unsupported exception, not dnae");
        }
        fail("installPackageForUser should have thrown an exception");
    }

    /**
     * Unit test for {@link NativeDevice#getInstalledPackageNames()}.
     */
    public void testGetInstalledPackageNames_exception() throws Exception {
        try {
            mTestDevice.getInstalledPackageNames();
        } catch (UnsupportedOperationException onse) {
            return;
        }
        fail("getInstalledPackageNames should have thrown an exception");
    }

    /**
     * Unit test for {@link NativeDevice#getScreenshot()}.
     */
    public void testGetScreenshot_exception() throws Exception {
        try {
            mTestDevice.getScreenshot();
        } catch (UnsupportedOperationException onse) {
            return;
        }
        fail("getScreenshot should have thrown an exception");
    }

    /**
     * Unit test for {@link NativeDevice#pushDir(File, String)}.
     */
    public void testPushDir_notADir() throws Exception {
        assertFalse(mTestDevice.pushDir(new File(""), ""));
    }

    /**
     * Unit test for {@link NativeDevice#pushDir(File, String)}.
     */
    public void testPushDir_childFile() throws Exception {
        mTestDevice = new TestableAndroidNativeDevice() {
            @Override
            public boolean pushFile(File localFile, String remoteFilePath)
                    throws DeviceNotAvailableException {
                return true;
            }
        };
        File testDir = FileUtil.createTempDir("pushDirTest");
        FileUtil.createTempFile("test1", ".txt", testDir);
        assertTrue(mTestDevice.pushDir(testDir, ""));
        FileUtil.recursiveDelete(testDir);
    }

    /**
     * Unit test for {@link NativeDevice#pushDir(File, String)}.
     */
    public void testPushDir_childDir() throws Exception {
        mTestDevice = new TestableAndroidNativeDevice() {
            @Override
            public String executeShellCommand(String cmd) throws DeviceNotAvailableException {
                return "";
            }
            @Override
            public boolean pushFile(File localFile, String remoteFilePath)
                    throws DeviceNotAvailableException {
                return false;
            }
        };
        File testDir = FileUtil.createTempDir("pushDirTest");
        File subDir = FileUtil.createTempDir("testSubDir", testDir);
        FileUtil.createTempDir("test1", subDir);
        assertTrue(mTestDevice.pushDir(testDir, ""));
        FileUtil.recursiveDelete(testDir);
    }

    private List<String> getFlatDir(File root) {
        List<String> ret = new ArrayList<>();
        for (File f : root.listFiles()) {
            if (f.isDirectory()) {
                String base = f.getName() + "/";
                ret.add(base);
                List<String> list = getFlatDir(f);
                for (String e :list) {
                    ret.add(base + e);
                }
            } else {
                ret.add(f.getName());
            }
        }
        return ret;
    }

    public void testPullDir() throws Exception {
        final String base = "/foo";
        final String[][] dirs = new String[][]{
            {base, "bar1/"},
            {base, "bar2/"},
            {base + "/bar1", ""},
            {base + "/bar2", "file1"},
            {base + "/bar2", "file2"},
            {base + "/bar2", "bar3/"},
            {base + "/bar2/bar3", "file1"},
        };
        final String lsCmd = "ls -Ap1 ";
        mTestDevice = new TestableAndroidNativeDevice() {
            @Override
            public String executeShellCommand(String command) throws DeviceNotAvailableException {
                if (!command.startsWith(command)) {
                    fail("unsupported shell command");
                    return null;
                }
                // assuming passed in command should always be a full directory path without
                // trailing "/"
                command = command.substring(lsCmd.length());
                StringBuilder ret = new StringBuilder();
                for (String[] item : dirs) {
                    if (item[0].equals(command)) {
                        if (item[1].isEmpty()) {
                            return "";
                        } else {
                            ret.append(item[1]);
                            ret.append('\n');
                        }
                    }
                }
                return ret.toString();
            }
            @Override
            public boolean isDirectory(String path) throws DeviceNotAvailableException {
                for (String[] item : dirs) {
                    if (item[0].equals(path)) {
                        return true;
                    }
                }
                return false;
            }
            @Override
            public boolean pullFile(String remoteFilePath, File localFile)
                    throws DeviceNotAvailableException {
                // check that remoteFilePath is valid
                boolean found = false;
                for (String[] item : dirs) {
                    if (String.format("%s/%s", item[0], item[1]).equals(remoteFilePath)) {
                        found = true;
                        break;
                    }
                }
                assertTrue("trying to pull non-existent file: " + remoteFilePath, found);
                try {
                    return localFile.createNewFile();
                } catch (IOException ioe) {
                    throw new RuntimeException("failed to create empty file", ioe);
                }
            }
        };
        File dir = FileUtil.createTempDir("tf-test");
        try {
            mTestDevice.pullDir(base, dir);
            // verify local directory structure
            Set<String> files = new HashSet<>();
            files.addAll(getFlatDir(dir));
            for (String[] item : dirs) {
                if (item[1].isEmpty()) {
                    // skip empty directories (already covered when listing parent directory)
                    continue;
                }
                String path = String.format("%s/%s", item[0], item[1]);
                // remove the "/foo/" prefix
                path = path.substring(base.length() + 1);
                if (!files.contains(path)) {
                    fail("unknown path: " + path);
                } else {
                    files.remove(path);
                }
            }
            assertTrue("failed validation: " + files.toString(), files.isEmpty());
        } finally {
            FileUtil.recursiveDelete(dir);
        }
    }

    /**
     * Unit test for {@link NativeDevice#getCurrentUser()}.
     */
    public void testGetCurrentUser_exception() throws Exception {
        try {
            mTestDevice.getScreenshot();
        } catch (UnsupportedOperationException onse) {
            return;
        }
        fail("getCurrentUser should have thrown an exception.");
    }

    /**
     * Unit test for {@link NativeDevice#getUserFlags(int)}.
     */
    public void testGetUserFlags_exception() throws Exception {
        try {
            mTestDevice.getUserFlags(0);
        } catch (UnsupportedOperationException onse) {
            return;
        }
        fail("getUserFlags should have thrown an exception.");
    }

    /**
     * Unit test for {@link NativeDevice#getUserSerialNumber(int)}.
     */
    public void testGetUserSerialNumber_exception() throws Exception {
        try {
            mTestDevice.getUserSerialNumber(0);
        } catch (UnsupportedOperationException onse) {
            return;
        }
        fail("getUserSerialNumber should have thrown an exception.");
    }

    /**
     * Unit test for {@link NativeDevice#switchUser(int)}.
     */
    public void testSwitchUser_exception() throws Exception {
        try {
            mTestDevice.switchUser(10);
        } catch (UnsupportedOperationException onse) {
            return;
        }
        fail("switchUser should have thrown an exception.");
    }

    /**
     * Unit test for {@link NativeDevice#switchUser(int, long)}.
     */
    public void testSwitchUserTimeout_exception() throws Exception {
        try {
            mTestDevice.switchUser(10, 5*1000);
        } catch (UnsupportedOperationException onse) {
            return;
        }
        fail("switchUser should have thrown an exception.");
    }

    /**
     * Unit test for {@link NativeDevice#stopUser(int)}.
     */
    public void testStopUser_exception() throws Exception {
        try {
            mTestDevice.stopUser(0);
        } catch (UnsupportedOperationException onse) {
            return;
        }
        fail("stopUser should have thrown an exception.");
    }

    /**
     * Unit test for {@link NativeDevice#stopUser(int, boolean, boolean)}.
     */
    public void testStopUserFlags_exception() throws Exception {
        try {
            mTestDevice.stopUser(0, true, true);
        } catch (UnsupportedOperationException onse) {
            return;
        }
        fail("stopUser should have thrown an exception.");
    }

    /**
     * Unit test for {@link NativeDevice#isUserRunning(int)}.
     */
    public void testIsUserIdRunning_exception() throws Exception {
        try {
            mTestDevice.isUserRunning(0);
        } catch (UnsupportedOperationException onse) {
            return;
        }
        fail("stopUser should have thrown an exception.");
    }

    /**
     * Unit test for {@link NativeDevice#hasFeature(String)}.
     */
    public void testHasFeature_exception() throws Exception {
        try {
            mTestDevice.hasFeature("feature:test");
        } catch (UnsupportedOperationException onse) {
            return;
        }
        fail("hasFeature should have thrown an exception.");
    }

    /**
     * Unit test for {@link NativeDevice#getSetting(String, String)}.
     */
    public void testGetSettingSystemUser_exception() throws Exception {
        try {
            mTestDevice.getSetting("global", "wifi_on");
        } catch (UnsupportedOperationException onse) {
            return;
        }
        fail("getSettings should have thrown an exception.");
    }

    /**
     * Unit test for {@link NativeDevice#getSetting(int, String, String)}.
     */
    public void testGetSetting_exception() throws Exception {
        try {
            mTestDevice.getSetting(0, "global", "wifi_on");
        } catch (UnsupportedOperationException onse) {
            return;
        }
        fail("getSettings should have thrown an exception.");
    }

    /**
     * Unit test for {@link NativeDevice#setSetting(String, String, String)}.
     */
    public void testSetSettingSystemUser_exception() throws Exception {
        try {
            mTestDevice.setSetting("global", "wifi_on", "0");
        } catch (UnsupportedOperationException onse) {
            return;
        }
        fail("putSettings should have thrown an exception.");
    }

    /**
     * Unit test for {@link NativeDevice#setSetting(int, String, String, String)}.
     */
    public void testSetSetting_exception() throws Exception {
        try {
            mTestDevice.setSetting(0, "global", "wifi_on", "0");
        } catch (UnsupportedOperationException onse) {
            return;
        }
        fail("putSettings should have thrown an exception.");
    }

    /**
     * Unit test for {@link NativeDevice#getAndroidId(int)}.
     */
    public void testGetAndroidId_exception() throws Exception {
        try {
            mTestDevice.getAndroidId(0);
        } catch (UnsupportedOperationException onse) {
            return;
        }
        fail("getAndroidId should have thrown an exception.");
    }

    /**
     * Unit test for {@link NativeDevice#getAndroidIds()}.
     */
    public void testGetAndroidIds_exception() throws Exception {
        try {
            mTestDevice.getAndroidIds();
        } catch (UnsupportedOperationException onse) {
            return;
        }
        fail("getAndroidIds should have thrown an exception.");
    }

    /**
     * Unit test for {@link NativeDevice#connectToWifiNetworkIfNeeded(String, String)}.
     */
    public void testConnectToWifiNetworkIfNeeded_alreadyConnected()
            throws DeviceNotAvailableException {
        EasyMock.expect(mMockWifi.checkConnectivity(mTestDevice.getOptions().getConnCheckUrl()))
                .andReturn(true);
        EasyMock.replay(mMockWifi);
        assertTrue(mTestDevice.connectToWifiNetworkIfNeeded(FAKE_NETWORK_SSID,
                FAKE_NETWORK_PASSWORD));
        EasyMock.verify(mMockWifi);
    }

    /**
     * Unit test for {@link NativeDevice#connectToWifiNetwork(String, String)}.
     */
    public void testConnectToWifiNetwork_success() throws DeviceNotAvailableException {
        EasyMock.expect(mMockWifi.connectToNetwork(FAKE_NETWORK_SSID, FAKE_NETWORK_PASSWORD,
                mTestDevice.getOptions().getConnCheckUrl())).andReturn(true);
        Map<String, String> fakeWifiInfo = new HashMap<String, String>();
        fakeWifiInfo.put("bssid", FAKE_NETWORK_SSID);
        EasyMock.expect(mMockWifi.getWifiInfo()).andReturn(fakeWifiInfo);
        EasyMock.replay(mMockWifi, mMockIDevice);
        assertTrue(mTestDevice.connectToWifiNetwork(FAKE_NETWORK_SSID,
                FAKE_NETWORK_PASSWORD));
        EasyMock.verify(mMockWifi, mMockIDevice);
    }

    /**
     * Unit test for {@link NativeDevice#connectToWifiNetwork(String, String)} for a failure
     * to connect case.
     */
    public void testConnectToWifiNetwork_failure() throws DeviceNotAvailableException {
        EasyMock.expect(mMockWifi.connectToNetwork(FAKE_NETWORK_SSID, FAKE_NETWORK_PASSWORD,
                mTestDevice.getOptions().getConnCheckUrl())).andReturn(false)
                .times(mTestDevice.getOptions().getWifiAttempts());
        Map<String, String> fakeWifiInfo = new HashMap<String, String>();
        fakeWifiInfo.put("bssid", FAKE_NETWORK_SSID);
        EasyMock.expect(mMockWifi.getWifiInfo()).andReturn(fakeWifiInfo)
                .times(mTestDevice.getOptions().getWifiAttempts());
        mMockRunUtil.sleep(EasyMock.anyLong());
        EasyMock.expectLastCall().times(mTestDevice.getOptions().getWifiAttempts() - 1);
        EasyMock.replay(mMockWifi, mMockIDevice, mMockRunUtil);
        assertFalse(mTestDevice.connectToWifiNetwork(FAKE_NETWORK_SSID,
                FAKE_NETWORK_PASSWORD));
        EasyMock.verify(mMockWifi, mMockIDevice, mMockRunUtil);
    }

    /**
     * Unit test for {@link NativeDevice#checkWifiConnection(String)}.
     */
    public void testCheckWifiConnection() throws DeviceNotAvailableException {
        EasyMock.expect(mMockWifi.isWifiEnabled()).andReturn(true);
        EasyMock.expect(mMockWifi.getSSID()).andReturn("\"" + FAKE_NETWORK_SSID + "\"");
        EasyMock.expect(mMockWifi.hasValidIp()).andReturn(true);
        EasyMock.expect(mMockWifi.checkConnectivity(mTestDevice.getOptions().getConnCheckUrl()))
                .andReturn(true);
        EasyMock.replay(mMockWifi, mMockIDevice);
        assertTrue(mTestDevice.checkWifiConnection(FAKE_NETWORK_SSID));
        EasyMock.verify(mMockWifi, mMockIDevice);
    }

    /**
     * Unit test for {@link NativeDevice#checkWifiConnection(String)} for a failure.
     */
    public void testCheckWifiConnection_failure() throws DeviceNotAvailableException {
        EasyMock.expect(mMockWifi.isWifiEnabled()).andReturn(false);
        EasyMock.replay(mMockWifi, mMockIDevice);
        assertFalse(mTestDevice.checkWifiConnection(FAKE_NETWORK_SSID));
        EasyMock.verify(mMockWifi, mMockIDevice);
    }

    /**
     * Unit test for {@link NativeDevice#isWifiEnabled()}.
     */
    public void testIsWifiEnabled() throws DeviceNotAvailableException {
        EasyMock.expect(mMockWifi.isWifiEnabled()).andReturn(true);
        EasyMock.replay(mMockWifi, mMockIDevice);
        assertTrue(mTestDevice.isWifiEnabled());
        EasyMock.verify(mMockWifi, mMockIDevice);
    }

    /**
     * Unit test for {@link NativeDevice#isWifiEnabled()} with runtime exception from
     * wifihelper.
     */
    public void testIsWifiEnabled_exception() throws DeviceNotAvailableException {
        EasyMock.expect(mMockWifi.isWifiEnabled()).andThrow(new RuntimeException());
        EasyMock.replay(mMockWifi, mMockIDevice);
        assertFalse(mTestDevice.isWifiEnabled());
        EasyMock.verify(mMockWifi, mMockIDevice);
    }

    /**
     * Unit test for {@link NativeDevice#disconnectFromWifi()}.
     */
    public void testDisconnectFromWifi() throws DeviceNotAvailableException {
        EasyMock.expect(mMockWifi.disconnectFromNetwork()).andReturn(true);
        EasyMock.replay(mMockWifi, mMockIDevice);
        assertTrue(mTestDevice.disconnectFromWifi());
        EasyMock.verify(mMockWifi, mMockIDevice);
    }

    /**
     * Unit test for {@link NativeDevice#enableNetworkMonitor()}.
     */
    public void testEnableNetworkMonitor() throws DeviceNotAvailableException {
        EasyMock.expect(mMockWifi.stopMonitor()).andReturn(null);
        EasyMock.expect(mMockWifi.startMonitor(EasyMock.anyLong(),
                EasyMock.eq(mTestDevice.getOptions().getConnCheckUrl()))).andReturn(true);
        EasyMock.replay(mMockWifi, mMockIDevice);
        assertTrue(mTestDevice.enableNetworkMonitor());
        EasyMock.verify(mMockWifi, mMockIDevice);
    }

    /**
     * Unit test for {@link NativeDevice#enableNetworkMonitor()} in case of failure.
     */
    public void testEnableNetworkMonitor_failure() throws DeviceNotAvailableException {
        EasyMock.expect(mMockWifi.stopMonitor()).andReturn(null);
        EasyMock.expect(mMockWifi.startMonitor(EasyMock.anyLong(),
                EasyMock.eq(mTestDevice.getOptions().getConnCheckUrl()))).andReturn(false);
        EasyMock.replay(mMockWifi, mMockIDevice);
        assertFalse(mTestDevice.enableNetworkMonitor());
        EasyMock.verify(mMockWifi, mMockIDevice);
    }

    /**
     * Unit test for {@link NativeDevice#disableNetworkMonitor()}.
     */
    public void testDisableNetworkMonitor() throws DeviceNotAvailableException {
        List<Long> samples = new ArrayList<Long>();
        samples.add(new Long(42));
        samples.add(new Long(256));
        samples.add(new Long(-1)); // failure to connect
        EasyMock.expect(mMockWifi.stopMonitor()).andReturn(samples);
        EasyMock.replay(mMockWifi, mMockIDevice);
        assertTrue(mTestDevice.disableNetworkMonitor());
        EasyMock.verify(mMockWifi, mMockIDevice);
    }

    /**
     * Unit test for {@link NativeDevice#reconnectToWifiNetwork()}.
     */
    public void testReconnectToWifiNetwork() throws DeviceNotAvailableException {
        EasyMock.expect(mMockWifi.checkConnectivity(mTestDevice.getOptions().getConnCheckUrl()))
                .andReturn(false);
        EasyMock.expect(mMockWifi.checkConnectivity(mTestDevice.getOptions().getConnCheckUrl()))
                .andReturn(true);
        mMockRunUtil.sleep(EasyMock.anyLong());
        EasyMock.expectLastCall();
        EasyMock.replay(mMockWifi, mMockIDevice, mMockRunUtil);
        try {
            mTestDevice.reconnectToWifiNetwork();
        } catch (NetworkNotAvailableException nnae) {
            fail("reconnectToWifiNetwork() should not have thrown an exception.");
        } finally {
            EasyMock.verify(mMockWifi, mMockIDevice, mMockRunUtil);
        }
    }

    /**
     * Unit test for {@link NativeDevice#isHeadless()}.
     */
    public void testIsHeadless() throws DeviceNotAvailableException {
        mTestDevice = new TestableAndroidNativeDevice() {
            @Override
            public String getProperty(String name) throws DeviceNotAvailableException {
                return "1\n";
            }
        };
        assertTrue(mTestDevice.isHeadless());
    }

    /**
     * Unit test for {@link NativeDevice#isHeadless()}.
     */
    public void testIsHeadless_notHeadless() throws DeviceNotAvailableException {
        mTestDevice = new TestableAndroidNativeDevice() {
            @Override
            public String getProperty(String name) throws DeviceNotAvailableException {
                return null;
            }
        };
        assertFalse(mTestDevice.isHeadless());
    }

    /**
     * Unit test for {@link NativeDevice#getDeviceDate()}.
     */
    public void testGetDeviceDate() throws DeviceNotAvailableException {
        mTestDevice = new TestableAndroidNativeDevice() {
            @Override
            public String executeShellCommand(String name) throws DeviceNotAvailableException {
                return "21692641\n";
            }
        };
        assertEquals(21692641, mTestDevice.getDeviceDate());
    }

    /**
     * Unit test for {@link NativeDevice#getDeviceDate()}.
     */
    public void testGetDeviceDate_wrongformat() throws DeviceNotAvailableException {
        mTestDevice = new TestableAndroidNativeDevice() {
            @Override
            public String executeShellCommand(String name) throws DeviceNotAvailableException {
                return "WRONG\n";
            }
        };
        assertEquals(0, mTestDevice.getDeviceDate());
    }
}