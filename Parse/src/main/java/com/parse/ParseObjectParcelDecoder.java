package com.parse;

import android.os.Parcel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This is a stateful implementation of {@link ParseParcelDecoder} that remembers which
 * {@code ParseObject}s have been decoded. When a pointer is found and we have already decoded
 * an instance for the same object id, we use the decoded instance.
 *
 * This is very similar to what {@link KnownParseObjectDecoder} does for JSON, and is meant to be
 * used with {@link ParseObjectParcelEncoder}.
 */
/* package */ class ParseObjectParcelDecoder extends ParseParcelDecoder {

  private Map<String, ParseObject> objects = new HashMap<>();

  public ParseObjectParcelDecoder() {}

  public void addKnownObject(ParseObject object) {
    objects.put(getObjectOrLocalId(object), object);
  }

  @Override
  protected ParseObject decodePointer(Parcel source) {
    String className = source.readString();
    String objectId = source.readString();
    if (objects.containsKey(objectId)) {
      return objects.get(objectId);
    }
    // Should not happen if using in conjunction with ParseObjectParcelEncoder .
    return ParseObject.createWithoutData(className, objectId);
  }

  /* package for tests */ String getObjectOrLocalId(ParseObject object) {
    return object.getObjectId() != null ? object.getObjectId() : object.getOrCreateLocalId();
  }
}
