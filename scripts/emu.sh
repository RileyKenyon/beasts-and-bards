#!/usr/bin/env bash
# Android emulator helper for Flutter testing. Usage: scripts/emu.sh <cmd> [avd]
set -euo pipefail
SDK="${ANDROID_HOME:-$HOME/Android/Sdk}"
EMU="$SDK/emulator/emulator"
ADB="$SDK/platform-tools/adb"
APP_DIR="$(cd "$(dirname "$0")/../beasts_and_bards" && pwd)"
avd="${2:-$("$EMU" -list-avds | head -1)}"

case "${1:-}" in
  list)  "$EMU" -list-avds; "$ADB" devices ;;
  start) # boot headless-optional: set HEADLESS=1; wait until Android finished booting
    if [ "${HEADLESS:-}" = 1 ]; then extra=(-no-window -no-audio); else extra=(); fi
    nohup "$EMU" -avd "$avd" -no-snapshot-save "${extra[@]}" >"${TMPDIR:-/tmp}/emu-$avd.log" 2>&1 &
    "$ADB" wait-for-device
    until [ "$("$ADB" shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')" = 1 ]; do sleep 2; done
    echo "booted: $avd" ;;
  stop)  "$ADB" emu kill ;;
  run)   cd "$APP_DIR" && flutter run -d emulator ;;
  logs)  "$ADB" logcat -v time flutter:V '*:S' ;;
  wipe)  "$EMU" -avd "$avd" -wipe-data ;;
  *) echo "usage: $0 {list|start|stop|run|logs|wipe} [avd]"; exit 1 ;;
esac
