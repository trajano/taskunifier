#!/bin/sh

if [ $# != 1 ]
then
	echo "Usage: $0 version"
	exit 1
fi

echo "Creating DMG file $DMGFILE"

BASEDIR=`dirname $0`
BASEDIR="$BASEDIR/.."
TARFILE="$BASEDIR/binaries/TaskUnifier_$1/TaskUnifier_$1_mac.tar"
DMGFILE="$BASEDIR/binaries/TaskUnifier_$1/TaskUnifier_$1_mac.dmg"

mkdir $BASEDIR/temp

tar -C $BASEDIR/temp -xf $TARFILE

rm -f $DMGFILE

hdiutil create -quiet -srcfolder $BASEDIR/temp/TaskUnifier.app $DMGFILE 2> /dev/null
hdiutil internet-enable -quiet -yes $DMGFILE 2> /dev/null

rm -rf $BASEDIR/temp

exit 0

