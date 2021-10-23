
package jp.micin.react.skyway;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.HashMap;
import java.util.Map;

public class SkyWayPeerManagerModule extends ReactContextBaseJavaModule implements SkyWayPeerObserver {

  private final ReactApplicationContext reactContext;

  private final Map<String, SkyWayPeer> peers;



  public SkyWayPeerManagerModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;

    this.peers = new HashMap<String, SkyWayPeer>();
  }

  @Override
  public String getName() {
    return "SkyWayPeerManager";
  }

  public SkyWayPeer getPeerById(String peerId) {
    return peers.get(peerId);
  }

  @ReactMethod
  public void create(String peerId, ReadableMap options, ReadableMap constraints) {
    SkyWayPeer peer = new SkyWayPeer(reactContext, peerId, options, constraints);
    peer.addObserver(this);

    peers.put(peerId, peer);
  }

  @ReactMethod
  public void dispose(String peerId) {
    SkyWayPeer peer = peers.get(peerId);

    if (peer != null) {
      peer.dispose();
      peer.removeObserver(this);
    }
  }

  @ReactMethod
  public void connect(String peerId) {
    SkyWayPeer peer = peers.get(peerId);

    if (peer != null) {
      peer.connect();
    }
  }

  @ReactMethod
  public void diconnect(String peerId) {
    SkyWayPeer peer = peers.get(peerId);

    if (peer != null) {
      peer.disconnect();
    }
  }

  @ReactMethod
  public void listAllPeers(String peerId, Callback callback) {
    SkyWayPeer peer = peers.get(peerId);

    if (peer != null) {
      peer.listAllPeers(callback);
    }
  }

  @ReactMethod
  public void call(String peerId, String targetPeerId) {
    SkyWayPeer peer = peers.get(peerId);

    if (peer != null) {
      peer.call(targetPeerId);
    }
  }

  @ReactMethod
  public void hangup(String peerId) {
    SkyWayPeer peer = peers.get(peerId);

    if (peer != null) {
      peer.hangup();
    }

  }

  @Override
  public void onPeerOpen(SkyWayPeer peer) {
    WritableMap peerParam = Arguments.createMap();
    peerParam.putString("id", peer.getPeer().identity());
    WritableMap params = Arguments.createMap();
    params.putMap("peer", peerParam);

    sendEvent("SkyWayPeerOpen", params);
  }

  @Override
  public void onPeerCall(SkyWayPeer peer) {
    WritableMap peerParam = Arguments.createMap();
    peerParam.putString("id", peer.getPeer().identity());
    WritableMap params = Arguments.createMap();
    params.putMap("peer", peerParam);

    sendEvent("SkyWayPeerCall", params);
  }

  @Override
  public void onPeerClose(SkyWayPeer peer) {
    WritableMap peerParam = Arguments.createMap();
    peerParam.putString("id", peer.getPeer().identity());
    WritableMap params = Arguments.createMap();
    params.putMap("peer", peerParam);

    sendEvent("SkyWayPeerClose", params);
  }

  @Override
  public void onPeerDisconnected(SkyWayPeer peer) {
    WritableMap peerParam = Arguments.createMap();
    peerParam.putString("id", peer.getPeer().identity());
    WritableMap params = Arguments.createMap();
    params.putMap("peer", peerParam);

    sendEvent("SkyWayPeerDisconnected", params);
  }

  @Override
  public void onPeerError(SkyWayPeer peer) {
    WritableMap peerParam = Arguments.createMap();
    peerParam.putString("id", peer.getPeer().identity());
    WritableMap params = Arguments.createMap();
    params.putMap("peer", peerParam);

    sendEvent("SkyWayPeerError", params);
  }

  @Override
  public void onLocalStreamOpen(SkyWayPeer peer) {}
  @Override
  public void onLocalStreamWillClose(SkyWayPeer peer) {}
  @Override
  public void onRemoteStreamOpen(SkyWayPeer peer) {}
  @Override
  public void onRemoteStreamWillClose(SkyWayPeer peer) {}

  @Override
  public void onMediaConnection(SkyWayPeer peer) {
    WritableMap peerParam = Arguments.createMap();
    peerParam.putString("id", peer.getPeer().identity());
    WritableMap params = Arguments.createMap();
    params.putMap("peer", peerParam);

    sendEvent("SkyWayPeerMediaConnection", params);
  }

  @Override
  public void onPeerStatusChange(SkyWayPeer peer) {
    WritableMap peerParam = Arguments.createMap();
    peerParam.putString("id", peer.getPeer().identity());
    WritableMap params = Arguments.createMap();
    params.putMap("peer", peerParam);
    params.putInt("status", peer.getPeerStatus().getInt());

    sendEvent("SkyWayPeerPeerStatusChange", params);
  }

  @Override
  public void onMediaConnectionStatusChange(SkyWayPeer peer) {
    WritableMap peerParam = Arguments.createMap();
    peerParam.putString("id", peer.getPeer().identity());
    WritableMap params = Arguments.createMap();
    params.putMap("peer", peerParam);
    params.putInt("status", peer.getMediaConnectionStatus().getInt());

    sendEvent("SkyWayPeerMediaConnectionStatusChange", params);
  }

  private void sendEvent(String eventName, WritableMap params) {
    reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, params);
  }


}