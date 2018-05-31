<p align="center" width="400">
    <img src="https://raw.githubusercontent.com/ed-george/TCGenius/master/files/logo.png">
</p>

--------------

[![GitHub release](https://img.shields.io/github/release/ed-george/TCGenius.svg)](https://github.com/ed-george/TCGenius/releases) [![Add to Discord](https://img.shields.io/badge/add%20to-discord-7289da.svg)](https://discordapp.com/oauth2/authorize?&client_id=448896569882705930&scope=bot&permissions=0) [![Support TCGenius](https://img.shields.io/badge/buy%20me%20a%20beer-via%20beerpay-f85d5d.svg)](https://beerpay.io/ed-george/TCGenius) [![GitHub issues](https://img.shields.io/github/issues/ed-george/TCGenius.svg)](https://github.com/ed-george/TCGenius/issues) [![GitHub license](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://raw.githubusercontent.com/ed-george/TCGenius/master/LICENSE)

--------------

## Commands 📣

* `!help` - Display available commands

* `!set`
	* `!set` - Get list of all sets
	* `!set SEARCHTERM` - List of sets matching the search term

* `!card`
	* `!card SEARCHTERM` - Card matching search term
	* `!card [CARD-ID]` - Card matching ID (e.g. `!card [xy1-1]`) 

<p align="center">
    <img src="https://raw.githubusercontent.com/ed-george/TCGenius/master/files/bot-example-1.png" width="353">
</p>

## Add to Discord 🤖 

### Automatically

To add this bot to your Discord server [click here](https://discordapp.com/oauth2/authorize?&client_id=448896569882705930&scope=bot&permissions=0) and authorise the bot to post messages, embed links and read channel messages.

**If you use and enjoy this bot on your server, please consider contributing a small donation to the upkeep.**

[![Support TCGenius](https://img.shields.io/badge/Click%20here%20to%20support-via%20beerpay-f85d5d.svg)](https://beerpay.io/ed-george/TCGenius)

### Manual Build

TCGenius requires Java 1.8 to run.

To create and run your own version of TCGenius, follow the steps below.


* Fork this project
* [Create a new application](https://discordapp.com/developers/applications/me/create) on Discord
* Create a `server.properties` file within `/src/resources`
* Add values for `server.token` and `server.ownerid` within the file. These values can be found from your newly Discord app's dashboard 
* Within the project's root folder, run the following within a terminal window
	* Windows: `gradlew shadowJar`
	* Mac / Linux: `./gradlew shadowJar`
* Copy the generated `.jar` file from `build/libs` and run it using `java -jar FILENAME.jar` 
* 🎉 Enjoy!

## Contributing 🛠

I welcome contributions and discussion for new features or bug fixes. It is recommended to file an issue first to prevent unnecessary efforts, but feel free to put in pull requests in the case of trivial changes. In any other case, please feel free to open discussion and I will get back to you when possible.

## Thanks 💕

This project uses the [Kotlin Pokemon TCG SDK](https://github.com/PokemonTCG/pokemon-tcg-sdk-kotlin) for [pokemontcg.io](https://pokemontcg.io/)

Logo designed by [Ray Cheung](https://twitter.com/dropstoproll) 🙌

