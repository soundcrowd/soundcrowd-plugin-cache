# soundcrowd-plugin-cache

[![android](https://github.com/soundcrowd/soundcrowd-plugin-cache/actions/workflows/android.yml/badge.svg)](https://github.com/soundcrowd/soundcrowd-plugin-cache/actions/workflows/android.yml)
[![GitHub release](https://img.shields.io/github/release/soundcrowd/soundcrowd-plugin-cache.svg)](https://github.com/soundcrowd/soundcrowd-plugin-cache/releases)
[![GitHub](https://img.shields.io/github/license/soundcrowd/soundcrowd.svg)](LICENSE)

This soundcrowd plugin adds caching support for online streaming services. Once a media file was streamed completely, it is cached locally and can be played directly again without using network traffic. The cache plugin adds its own category where all cached files can be accessed. The caching process greatly depends on the `AndroidVideoCache` library, thanks!

## Building

    $ git clone --recursive https://github.com/soundcrowd/soundcrowd-plugin-cache
    $ cd soundcrowd-plugin-cache
    $ ./gradlew assembleDebug

Install via ADB:

    $ adb install cache/build/outputs/apk/debug/cache-debug.apk

## License

Licensed under GPLv3.

## Dependencies

- [AndroidVideoCache](https://github.com/danikula/AndroidVideoCache) - Apache 2.0