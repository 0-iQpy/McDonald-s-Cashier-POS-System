# UI/UX Guide - McDonald's POS System

## Login Screen (LoginView)
```
┌─────────────────────────────────┐
│                                 │
│    CASHIER LOGIN                │
│                                 │
│  Cashier Name: [___________]    │
│  Password:     [***********]    │
│                                 │
│       [ Login ]                 │
│                                 │
└─────────────────────────────────┘
```

---

## Cashier Main Screen (CashierView) - NEW DESIGN

```
┌──────────────────────────────────────────────────────────────┐
│            McDonald's POS - Cashier View                     │
├──────────────────────────────────────────────────────────────┤
│                   Welcome, Kai                               │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  Menu Items - Click to Select                               │
│  ┌──────────────────────────────────────────────────────┐   │
│  │ Big Mac Meal                              ₱950.00   │   │
│  │ [−] 0 [+]                                           │   │
│  │──────────────────────────────────────────────────────│   │
│  │ McSpicy Sandwich                          ₱399.00   │   │
│  │ [−] 0 [+]                                           │   │
│  │──────────────────────────────────────────────────────│   │
│  │ Happy Meal                                ₱250.50   │   │
│  │ [−] 0 [+]                                           │   │
│  │──────────────────────────────────────────────────────│   │
│  │ Medium Coke                               ₱89.90    │   │
│  │ [−] 0 [+]                                           │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                              │
├──────────────────────────────────────────────────────────────┤
│  [ Apply Discount ] ✓ Senior/PWD Discount (15%)  [ Admin ]  │
│  Payment Amount (₱): [___________]                           │
│  [ Preview Receipt ]  [ Complete Order ]  [ Logout ]         │
└──────────────────────────────────────────────────────────────┘
```

### Key Features:
- Click/hover items for interactive feedback
- +/− buttons next to quantity
- One-click discount application
- Separate preview and complete buttons

---

## Admin Authentication Dialog

```
┌───────────────────────────────────┐
│   Admin Authentication            │
├───────────────────────────────────┤
│                                   │
│  Username: [_____________]        │
│                                   │
│  Password: [_____________]        │
│                                   │
│  [ Login ]  [ Cancel ]            │
│                                   │
└───────────────────────────────────┘
```

### When appears:
- Clicking "Apply Discount" button
- Clicking "Admin" button

---

## Receipt Preview (ReceiptView) - UPDATED

```
┌────────────────────────────────────┐
│    Receipt Preview                 │
├────────────────────────────────────┤
│ ************************************│
│              McDonald's            │
│                Calamba             │
│ ************************************│
│                                    │
│ Date: 2025-12-06                   │
│ Time: 14:30:45                     │
│ Cashier: Kai                       │
│                                    │
│ Qty  Item                    Price │
│ ────────────────────────────────── │
│ 1    Big Mac Meal          ₱950.00 │
│ 2    Happy Meal            ₱501.00 │
│ ────────────────────────────────── │
│ Subtotal:                  ₱1230.00│
│ VAT (12%):                 ₱147.60 │
│ Discount:                   ₱0.00  │
│ Total:                     ₱1377.60│
│                                    │
│ Payment:                   ₱1500.00│
│ Change:                    ₱122.40 │
│                                    │
│ [✓ Complete Order]  [Cancel]       │
└────────────────────────────────────┘
```

### New Features:
- Complete Order button (not just display)
- Cancel option to discard order
- Clear instructions

---

## Admin View - Transaction Management (AdminView) - NEW

```
┌──────────────────────────────────────────────────────────────┐
│  Admin - Transaction Management                              │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  TxnID  Cashier  Date           Subtotal   VAT    Discount │
│  ────  ────────  ──────────  ──────────────────────────────│
│  15    1         2025-12-06  1230.00     147.60    0.00    │
│  14    1         2025-12-06  2050.00     0.00     307.50   │
│  13    1         2025-12-05  950.00      114.00    0.00    │
│  12    1         2025-12-05  1500.00     0.00     225.00   │
│  ...                                                       │
│                                                              │
├──────────────────────────────────────────────────────────────┤
│                [ Refresh ]  [ Delete Selected ]              │
└──────────────────────────────────────────────────────────────┘
```

### Features:
- View all transactions
- Sort by date
- Delete with confirmation
- Refresh to reload data

---

## Order Flow Diagram

```
┌─────────────────┐
│  Login Screen   │
└────────┬────────┘
         │
         ↓
┌─────────────────────────┐
│  Cashier View           │
│  - Click items          │
│  - Use +/− buttons      │
└────────┬────────────────┘
         │
         ├─→ [ Apply Discount ] → Admin Auth → Discount Applied
         │
         ↓
┌─────────────────────────┐
│  Enter Payment Amount   │
└────────┬────────────────┘
         │
         ↓
┌─────────────────────────┐
│ [ Preview Receipt ]     │
│ Shows full order        │
└────────┬────────────────┘
         │
         ├─→ [ Complete Order ] → Save to DB → Reset View
         │
         └─→ [ Cancel ] → Back to Cashier View
```

---

## Admin Access Flow

```
┌─────────────────┐
│  Cashier View   │
└────────┬────────┘
         │
         ├─→ [ Apply Discount ] → Admin Auth → Success
         │
         ├─→ [ Admin ] → Admin Auth → Success
         │              │
         │              ↓
         │       ┌──────────────────────┐
         │       │  AdminView           │
         │       │  - View Transactions │
         │       │  - Delete Tx (Confirm)
         │       └──────────────────────┘
         │
         └─→ [ Logout ] → Back to Login

```

---

## Item Selection UX

```
┌──────────────────────────────────────┐
│ Big Mac Meal               ₱950.00   │  ← Not Selected
│ [−] 0 [+]                            │
└──────────────────────────────────────┘

┌──────────────────────────────────────┐ ← Hover/Selected
│ Big Mac Meal               ₱950.00   │  
│ [−] 3 [+]                            │
└──────────────────────────────────────┘
  ↑       ↑   ↑
  │       │   │
  │       │   └─ Plus button
  │       └───── Current quantity
  └───────────── Minus button
```

---

## Color Scheme

| Element | Color | Usage |
|---------|-------|-------|
| Item Price | Green | `Color(0, 100, 0)` |
| Panel Background | Light Gray | `Color(240, 240, 240)` |
| Hover State | Lighter Gray | `Color(220, 220, 220)` |
| Complete Button | Green | `Color(0, 150, 0)` |
| Discount Applied | Green | `Color(0, 150, 0)` |
| No Discount | Red | `Color.RED` |
| Border | Gray | `Color.GRAY` |

---

## Keyboard Shortcuts (Future Enhancement)

Could add:
- `Enter` - Complete Order
- `Esc` - Cancel
- `D` - Apply Discount
- `A` - Open Admin View
- `L` - Logout

---

## Responsive Design Notes

- Main window: 1000x700 (resizable)
- Receipt window: 500x600 (fixed)
- Admin window: 1000x600 (resizable)
- All layouts use FlowLayout and BorderLayout for responsiveness
- ScrollPane for long item lists

---

## Accessibility Features

- Large fonts for readability
- Clear button labels
- Color-coded status (discount applied/not applied)
- Cursor changes to hand over interactive elements
- Hover feedback on all buttons
- Clear error messages
- Confirmation dialogs for destructive actions

---

## Success Indicators

✅ Item selected - Color changes on hover  
✅ Discount applied - Green text indicator  
✅ Admin authenticated - Admin view opens  
✅ Order completed - Success message + reset  
✅ Transaction deleted - Confirmation + refresh

---

This design prioritizes:
- **Intuitiveness** - Clear visual hierarchy
- **Efficiency** - Quick order entry
- **Safety** - Confirmations for important actions
- **Accessibility** - Easy to read and use
