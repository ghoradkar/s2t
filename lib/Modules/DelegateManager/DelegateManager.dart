// Define delegate contract (protocol)
// ignore_for_file: file_names

abstract class RefreshDelegate {
  void callRefreshAPI();
}

class DelegateManager {
  // Singleton instance
  static final DelegateManager _instance = DelegateManager._internal();
  factory DelegateManager() => _instance;
  DelegateManager._internal();

  RefreshDelegate? _delegate;

  void registerDelegate(RefreshDelegate delegate) {
    _delegate = delegate;
  }

  void unregisterDelegate() {
    _delegate = null;
  }

  void triggerRefresh() {
    _delegate?.callRefreshAPI();
  }
}
