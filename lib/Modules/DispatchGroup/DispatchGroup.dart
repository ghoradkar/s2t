// ignore_for_file: file_names

// import 'dart:async';

// class DispatchGroup {
//   int _taskCount = 0;
//   final Completer<void> _completer = Completer<void>();

//   void enter() {
//     _taskCount++;
//   }

//   void leave() {
//     _taskCount--;
//     if (_taskCount == 0 && !_completer.isCompleted) {
//       _completer.complete();
//     }
//   }

//   void notify(void Function() callback) {
//     _completer.future.then((_) => callback());
//   }

//   Future<void> wait() => _completer.future;
// }

import 'dart:async';

class DispatchGroup {
  // 🔹 Step 1: Private constructor
  DispatchGroup._internal();

  // 🔹 Step 2: Single static instance
  static final DispatchGroup _instance = DispatchGroup._internal();

  // 🔹 Step 3: Factory constructor (returns same instance always)
  factory DispatchGroup() => _instance;

  // Internal state
  int _taskCount = 0;
  Completer<void> _completer = Completer<void>();

  /// Called before starting an async task
  void enter() {
    _taskCount++;
  }

  /// Called when an async task completes
  void leave() {
    _taskCount--;
    if (_taskCount == 0 && !_completer.isCompleted) {
      _completer.complete();
    }
  }

  /// Called to execute a callback after all tasks finish
  void notify(void Function() callback) {
    _completer.future.then((_) => callback());
  }

  /// Optional: Waits for all tasks to complete
  Future<void> wait() => _completer.future;

  /// Optional: Reset group (for reuse)
  void reset() {
    if (!_completer.isCompleted) {
      _completer.complete(); // complete old one safely
    }
    _taskCount = 0;
    _completer = Completer<void>();
  }
}
