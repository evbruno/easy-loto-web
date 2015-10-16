[![Build Status](https://travis-ci.org/evbruno/easy-loto-web.svg)](https://travis-ci.org/evbruno/easy-loto-web)

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

## Comandos supimpas pro Heroku e afins

*Bash*

```
$ heroku bash
$ heroku run ls -la
$ heroku ps
```

*Logs:*

```
$ heroku logs
$ heroku logs --tail
$ heroku logs -t
$ heroku logs -a NOME_DA_APP
$ heroku logs --ps scheduler -a NOME_DA_APP
```

*Misc:*

```
$ brew install heroku
```

```
$ heroku auth
$ heroku open
```
