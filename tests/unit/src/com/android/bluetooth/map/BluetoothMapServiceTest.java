/*
 * Copyright 2018 The Android Open Source Project
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
package com.android.bluetooth.map;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.filters.MediumTest;
import androidx.test.rule.ServiceTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.android.bluetooth.R;
import com.android.bluetooth.TestUtils;
import com.android.bluetooth.btservice.AdapterService;
import com.android.bluetooth.btservice.storage.DatabaseManager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class BluetoothMapServiceTest {
    private BluetoothMapService mService = null;
    private BluetoothAdapter mAdapter = null;
    private Context mTargetContext;

    @Rule public final ServiceTestRule mServiceRule = new ServiceTestRule();

    @Mock private AdapterService mAdapterService;
    @Mock private DatabaseManager mDatabaseManager;

    @Before
    public void setUp() throws Exception {
        mTargetContext = InstrumentationRegistry.getTargetContext();
        Assume.assumeTrue("Ignore test when BluetoothMapService is not enabled",
                mTargetContext.getResources().getBoolean(R.bool.profile_supported_map));
        MockitoAnnotations.initMocks(this);
        TestUtils.setAdapterService(mAdapterService);
        doReturn(mDatabaseManager).when(mAdapterService).getDatabase();
        doReturn(true).when(mAdapterService).isStartedProfile(anyString());
        TestUtils.startService(mServiceRule, BluetoothMapService.class);
        mService = BluetoothMapService.getBluetoothMapService();
        Assert.assertNotNull(mService);
        // Try getting the Bluetooth adapter
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        Assert.assertNotNull(mAdapter);
    }

    @After
    public void tearDown() throws Exception {
        if (!mTargetContext.getResources().getBoolean(R.bool.profile_supported_map)) {
            return;
        }
        doReturn(false).when(mAdapterService).isStartedProfile(anyString());
        TestUtils.stopService(mServiceRule, BluetoothMapService.class);
        mService = BluetoothMapService.getBluetoothMapService();
        Assert.assertNull(mService);
        TestUtils.clearAdapterService(mAdapterService);
    }

    @Test
    public void testInitialize() {
        Assert.assertNotNull(BluetoothMapService.getBluetoothMapService());
    }
}
