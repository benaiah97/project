#!/bin/ksh

set -x
 
# Archive Log Files Script 
# Author:  Todd Lewis
# Date:  11/19/04 
# This script takes either a single file or all of the files ending in -suffix
# that exist in -dir and moves them to -arch, appending the date to the end of the
# file.  All files moved to the log archive are compressed and aged out according
# to -days.

# Command line arguments (if not present, use <<defaults>>)
# -dir = the directory log files are in  <<NO DEFAULT>>
# -arch = the archive target directory <<value of -dir>>
# -days = the number of days to keep old log files <<30>>
# -suffix = the . suffix for a log file <<log>>
# -name = the log file name to archive

# FUNCTION:  Present the usage when asked.
function showUsage {
  echo " "
  echo "Usage: archiveLogs.sh -dir d1 -suffix s|-name n [-days d] [-arch d2] [-nocompress]"
  echo " -dir         The directory in which the log files reside. (Required: no default)"
  echo " -suffix      The . suffix for a log file name (no default)"
  echo " -name        The file name of the log to be archived. (no default)"
  echo " -days        Number of days to keep old archive files (Default: forever)"
  echo " -arch        The archive target directory (Default: value of -dir)" 
  echo " -nocompress  Do not compress the files using gzip -9 (Default action:  compress) "
  echo " "
  echo " Notes:  Must choose either -suffix or -name.  "
  echo "         -dir and -arcdir can be relative or absolute. "
  echo "         -days value must be between 1 and 365, inclusive."
  echo "         If days > 0 and name is specified, files with name* are aged out of archive."
  echo "         If days > 0 and suffix specified, files with *suffix* are aged out of archive."
  echo " "
}

# "Advance" Declarations/Variable set-up
DIR="NULL"
SUFFIX="NULL"
NAME="NULL"
DAYS="0"
ARCH="NULL"
COMPRESS="TRUE"

# FUNCTION:  Archive the file in DIR/NAME to ARCH/NAME.date.gz
#            cp /dev/null to existing DIR/NAME  
function archiveFile {
  set -x
  ARCHFILE=$ARCH/$NAME.`date +"%Y%m%d"`
  if [ -w $DIR/$NAME ]; then
     cp $DIR/$NAME $ARCHFILE
  else
     echo "WARN:  Archive NOT possible on unwritable file " $DIR/$NAME
     return
  fi
  cp /dev/null $DIR/$NAME
  echo "INFO:  Archive copy complete on " $DIR/$NAME
  if [ COMPRESS = "FALSE" ]; then
     return
  else
     gzip -9 $ARCHFILE
     chmod 644 $ARCHFILE.gz
  fi
}

# FUNCTION:  Erase ARCH/*.SUFFIX.* files older than DAYS.
#            If DAYS = 0, then never erase.
function eraseOldSuffix {
  set -x
  if [ $DAYS -eq 0 ]; then
     return
  fi
  let MTIMEARG=$DAYS-1
  find $ARCH -name "*.$SUFFIX.*" -mtime +$MTIMEARG | xargs rm
}

# FUNCTION:  Erase ARCH/name.* files older than DAYS.
#            If DAYS = 0, then never erase.
function eraseOldName {
  set -x
  if [ $DAYS -eq 0 ]; then
     return
  fi
  let MTIMEARG=$DAYS-1
  find $ARCH -name "$NAME.*" -mtime +$MTIMEARG | xargs rm
}

# Parameter edits
# Need 4 arguments at a minimum (-dir, dirname, -suffix or -name, value), 9 max.
if [ $# -lt 4 ]; then 
   echo "ERROR:  Invalid number of arguments." 
   showUsage
   exit 1
elif [ $# -gt 9 ]; then
   echo "ERROR:  Invalid number of arguments." 
   showUsage
   exit 1
elif [ $1 != "-dir" ]; then
   echo "ERROR:  Must include -dir with -suffix or -name."
   showUsage 
   exit 1
fi

# Need suffix or name
DIR=$2

if [ $3 = "-suffix" ]; then
   SUFFIX=$4
elif [ $3 = "-name" ]; then
   NAME=$4
else
   echo "ERROR:  Must include -suffix or -name (case sensitive)"
   showUsage
   exit 1
fi

# Evaluate remaining arguements (if any) 
if [ $# -eq 5 ]; then
   if [ $5 = "-nocompress" ]; then
     COMPRESS="FALSE"
   else
     echo "ERROR:  Invalid request format. (expected -nocompress)"
     showUsage
     exit 1
   fi
fi

if [ $# -eq 6 ]; then
   if [ $5 = "-days" ]; then
      DAYS=$6
   elif [ $5 = "-arch" ]; then
      ARCH=$6
   else
      echo "ERROR:  Invalid request format. (expected -days OR -arch)"
      showUsage
      exit 1
   fi
fi

if [ $# -eq 7 ]; then
   if [ $7 = "-nocompress" ]; then
      COMPRESS="FALSE"
   else
      echo "ERROR:  Invalid request format. (expected -nocompress)"
      showUsage
      exit 1
   fi
   if [ $5 = "-days" ]; then
      DAYS=$6
   elif [ $5 = "-arch" ]; then
      ARCH=$6
   else
      echo "ERROR:  Invalid request format. (expected -days or -arch)"
      showUsage
      exit 1
   fi
fi

if [ $# -gt 7 ]; then
   if [ $5 = "-days" ]; then
      DAYS=$6
   else
      echo "ERROR:  Invalid request format. (expected -days)"
      showUsage
      exit 1
   fi
   if [ $7 = "-arch" ]; then 
      ARCH=$8
   else      
      echo "ERROR:  Invalid request format. (expected -arch)"
      showUsage
      exit 1
   fi
   if [ $# -eq 9 ]; then
      if [ $9 = "-nocompress" ]; then
         COMPRESS="FALSE"
      else
         echo "ERROR:  Invalid request format. (expected -nocompress)"
         showUsage
         exit 1
      fi
   fi   
fi

# Force DAYS to numeric
let DAYS=$DAYS+0

if [ $DAYS -lt 0 ]; then
   echo "ERROR:  Number of days cannot be less than zero."
   showUsage
   exit 1
fi

# Force ARCH to become DIR if NULL
if [ $ARCH = "NULL" ]; then
   ARCH=$DIR
fi

echo " "
echo "VARIABLE     VALUE"
echo "=========   ======"
echo "DIR      = " $DIR
echo "SUFFIX   = " $SUFFIX
echo "NAME     = " $NAME
echo "DAYS     = " $DAYS
echo "ARCH     = " $ARCH
echo "COMPRESS = " $COMPRESS
echo "Passed Edits!!"
echo " "

# If being called for a single name, archive that file.
if [ $SUFFIX = "NULL" ]; then
   archiveFile
else
   cd $DIR
   LIST=`ls *.$SUFFIX`
   cd $OLDPWD
   echo $LIST
   for x in $LIST
   do 
     NAME=$x
     archiveFile 
   done
fi

# Archive old compressed log files in the archive directory
if [ $DAYS -eq 0 ]; then
   exit
else
   if [ $SUFFIX = "NULL" ]; then
      eraseOldName
   else
      eraseOldSuffix
   fi
fi 
