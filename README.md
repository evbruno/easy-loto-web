# Easy Loto

## Usage

Start services with sbt:

```
$ sbt
> ~re-start
```

## Testing

Execute tests using `test` command:

```
$ sbt
> test
```

## WebDev tips
 
**Start the _api server_ and run _another server_ for the web (html/js/css) dev.**
  
```bash
 $ npm install -g live-server
 $ cd src/main/resources/www
 $ live-server --port=9001
```

## Extras

https://github.com/jrudolph/sbt-dependency-graph

## Heroku dev

```bash
$ heroku buildpacks:clear -a easy-loto-dev
$ heroku buildpacks:add https://github.com/heroku/heroku-buildpack-nodejs.git -a easy-loto-dev
$ heroku buildpacks:add https://github.com/heroku/heroku-buildpack-scala.git -a easy-loto-dev
$ heroku buildpacks -a easy-loto-dev
$ heroku config:set NPM_CONFIG_LOGLEVEL=verbose -a easy-loto-dev
```
