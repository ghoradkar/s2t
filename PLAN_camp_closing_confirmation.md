# Camp Closing Confirmation — Implementation Plan

## Context
- Feature: When dashboard is "Door to Door (D2D)" and both check-in + check-out photos are uploaded,
  a **"Camp Closing Confirmation"** button appears on the Team Photos screen.
- Currently that button just shows a toast: `"Camp Closing Confirmation coming soon"`.
- Task: Wire it to navigate to the existing `CampClosingScreen`.

---

## Files to Edit

### 1. `lib/Screens/team_photos/screen/team_photos_screen.dart`

#### What to change
Replace the stub `_onCampClosingTap` method (line ~304–307) with a real navigation call:

```dart
static void _onCampClosingTap(BuildContext context, TeamPhotosController c) {
  final campIdStr = c.selectedCamp.value?.campId ?? '';
  final campId = int.tryParse(campIdStr) ?? 0;
  final distLgd = int.tryParse(c.distLgdCode) ?? 0;
  Navigator.push(
    context,
    MaterialPageRoute(
      builder: (_) => CampClosingScreen(
        campID: campId,
        campDate: c.selectedDate.value,
        dISTLGDCODE: distLgd,
      ),
    ),
  );
}
```

#### Add import at top of file
```dart
import 'package:s2toperational/Screens/health_screening_details/screens/camp_closing_screen/camp_closing_screen.dart';
```

#### Where the button lives (for reference)
```dart
// team_photos_screen.dart ~line 174–182
Obx(() {
  if (c.bothPhotosUploaded) {
    if (c.isD2DOrMMU) {
      return AppActiveButton(
        buttontitle: 'Camp Closing Confirmation',
        onTap: () => _onCampClosingTap(context, c),  // <-- this method
      );
    }
    return const SizedBox.shrink();
  }
  ...
```

---

## Data available in TeamPhotosController

| Parameter needed by CampClosingScreen | Source in TeamPhotosController |
|---|---|
| `campID` (int) | `int.tryParse(c.selectedCamp.value?.campId ?? '') ?? 0` |
| `campDate` (String) | `c.selectedDate.value` — already formatted as `dd/MM/yyyy` |
| `dISTLGDCODE` (int) | `int.tryParse(c.distLgdCode) ?? 0` (set in `onInit` from user session) |

---

## CampClosingScreen — existing contract

**File:** `lib/Screens/health_screening_details/screens/camp_closing_screen/camp_closing_screen.dart`

```dart
class CampClosingScreen extends StatefulWidget {
  final int campID;
  final String campDate;
  final int dISTLGDCODE;
  ...
}
```

It uses `CampClosingController.loadData(campId, distLgdCode, campDate)` which calls:
- `getCampDetailsCountAPI(CampId, DISTLGDCODE, FromDate, ToDate)` — D2D base URL
- `getCampCloseDetailsAPI(CampId)` — ConstructionWorker base URL
- `getConsumableListDetailsAPI(CampId)` — ConstructionWorker base URL

No changes needed to `CampClosingScreen` or its controller.

---

## Summary of changes

| File | Change |
|---|---|
| `team_photos_screen.dart` | Add import for `CampClosingScreen`; replace `_onCampClosingTap` stub with `Navigator.push` to `CampClosingScreen` |

That is the **only** file that needs editing. No new files, no new APIs, no controller changes.
