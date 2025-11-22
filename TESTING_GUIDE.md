# Testing Guide - Piggery App

## Quick Start

### Recommended Method: Android Studio

1. **Install Android Studio** from https://developer.android.com/studio

2. **Open the Project**
   - Launch Android Studio
   - Select "Open" or "Open an Existing Project"
   - Navigate to this folder and select it

3. **Sync Gradle**
   - When prompted, click "Sync Now"
   - Wait for dependencies to download (first time: 5-10 minutes)

4. **Create Emulator** (if needed)
   - Tools → Device Manager → Create Device
   - Choose: Pixel 6 or any phone
   - System Image: API 34 (Android 14) or minimum API 24
   - Click Finish

5. **Run the App**
   - Select emulator from device dropdown
   - Click green Run button (▶️)
   - App will build and launch

---

## What to Test

### 1. **Main Screen (Pig List)**

When you first launch the app, you'll see:
- Empty state with message "No pigs registered yet"
- A floating action button (+) at the bottom right
- Search bar at the top

**Test Actions:**
- ✓ App launches without crashes
- ✓ Empty state displays correctly
- ✓ Click the + button to add a pig

---

### 2. **Register Pig Screen**

After clicking the + button, you'll see a registration form:

**Test Creating a New Pig:**

1. **Photo Section** (top card)
   - Click "TAKE PHOTO" button
   - Grant camera permission when prompted
   - Take a photo of anything
   - Photo should display in the ImageView
   - Click "REMOVE PHOTO" to test removal

2. **Fill in Details:**
   - Tag Number: e.g., "001" (required, must be unique)
   - Breed: e.g., "Yorkshire" (required)
   - Gender: Select Male or Female
   - Date of Birth: Click to open date picker, select a date
   - Weight: e.g., "75.5" (required, positive number)
   - Status: Select from dropdown (Active/Sold/Deceased/Quarantine)
   - Notes: Optional text

3. **Save the Pig:**
   - Click "SAVE" button
   - Should see "Pig saved successfully" toast message
   - Should return to main screen
   - Pig should now appear in the list

**Test Validation:**

Try these to test error handling:
- Leave Tag Number empty → Should show "Tag number is required"
- Leave Breed empty → Should show "Breed is required"
- Enter invalid weight (0 or negative) → Should show error
- Create pig with same tag number → Should show "Tag number already exists"

---

### 3. **Pig List Screen**

After creating pigs, test the list:

**What You'll See:**
- Each pig shown in a card with:
  - Photo (if added) or placeholder icon
  - Tag number (e.g., "Tag #001")
  - Breed name
  - Gender (MALE or FEMALE)
  - Age in days
  - Weight in kg
  - Status badge with color coding:
    - Green = Active
    - Blue = Sold
    - Red = Deceased
    - Orange = Quarantine

**Test Actions:**
- ✓ Create multiple pigs (try 3-5)
- ✓ Verify all details display correctly
- ✓ Check age calculation updates
- ✓ Verify status colors are correct

---

### 4. **Search Functionality**

In the search bar at the top:

**Test Searches:**
- Type a tag number (e.g., "001") → Should filter to matching pigs
- Type a breed (e.g., "Yorkshire") → Should show matching breeds
- Type partial text → Should show partial matches
- Clear search → Should show all pigs again
- Type non-existent text → List should be empty

---

### 5. **Edit Pig**

Click on any pig card in the list:

**Test Editing:**
- Should open the same registration form
- All fields should be pre-filled with current data
- Change any field (e.g., update weight)
- Click "SAVE"
- Should see "Pig updated successfully"
- Return to list and verify changes appear
- Try changing tag number to duplicate → Should prevent with error

---

### 6. **Offline Capability**

This is already working! Test it:

**Test Offline:**
- Create some pigs
- Close the app completely (swipe away from recent apps)
- Turn OFF Wi-Fi and Mobile Data
- Reopen the app
- ✓ All pigs should still be there
- ✓ Can create new pigs
- ✓ Can edit existing pigs
- ✓ Can search pigs
- ✓ Everything works without internet!

---

### 7. **Photo Handling**

Test photo features thoroughly:

**Test Cases:**
- Take a photo → Should display correctly
- Photo orientation → Should auto-rotate if needed
- Remove photo → Should show placeholder
- Edit pig with photo → Photo should persist
- Take new photo when editing → Old photo should be replaced

---

## Expected Results Summary

| Feature | Expected Behavior |
|---------|-------------------|
| **App Launch** | Shows empty state or pig list |
| **Add Pig** | Opens registration form |
| **Save Pig** | Validates, saves, returns to list |
| **Pig List** | Shows all pigs with photos and details |
| **Search** | Filters pigs by tag/breed in real-time |
| **Edit Pig** | Loads data, allows updates |
| **Delete Photo** | Removes photo, shows placeholder |
| **Camera** | Requests permission, captures photo |
| **Offline** | Works completely without internet |
| **Validation** | Shows errors for invalid input |

---

## Known Limitations (Current Version)

✓ Implemented:
- Pig registration and management
- Photo documentation
- Offline storage
- Search functionality

⏳ Not Yet Implemented:
- Farrowing management
- Weaning tracking
- Feeding schedules
- Health records
- Cloud backup/sync
- Export data
- Statistics/reports

---

## Troubleshooting

### App Won't Build
- Ensure Android Studio is up to date
- File → Invalidate Caches → Invalidate and Restart
- Check internet connection for Gradle sync

### Emulator Won't Start
- Ensure virtualization is enabled in BIOS
- Try creating a new emulator with different API level
- Check available disk space (needs 8GB+)

### Camera Not Working
- Ensure camera permission is granted
- In emulator, camera simulates with test pattern
- On physical device, use real camera

### Database Issues
- To reset: Uninstall app from device/emulator
- Reinstall from Android Studio
- Fresh install creates new database

---

## Performance Testing

The app should be responsive:
- List with 100 pigs → Smooth scrolling
- Search → Instant filtering
- Photo loading → Smooth with Glide cache
- Database operations → Fast with Room

---

## Next Steps After Testing

Once you've tested the current features, we can add:
1. Farrowing management
2. Weaning tracking
3. Feeding schedules
4. Health management

Happy Testing! 🐷
