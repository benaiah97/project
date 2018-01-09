@setlocal

@SET PATH=%M2_HOME%\bin;%PATH%

@REM run maven plugin org.hybridlabs.beautifier from beautifier plugin.
@REM run goal beautify-imports in phase process-sources

@REM mvn beautifier:beautify-imports

mvn org.andromda.maven.plugins:andromda-beautifier-plugin:3.4.2-SNAPSHOT:beautify-imports

@endlocal
