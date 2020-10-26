from rest_framework import serializers
from MobApp.models import Character, Race, Stat

class RaceSerializer(serializers.ModelSerializer):
    class Meta:
        model = Race
        fields = (
            'race_id',
            'race_name',
            'race_description'
            )

class CharacterSerializer(serializers.ModelSerializer):
    race = serializers.SlugRelatedField(
        queryset=Race.objects.all(), slug_field='race_name'
    )
    class Meta:
        model = Character
        fields = (
            'character_id',
            'character_name',
            'character_description',
            'race',
            'creation_date',
            'character_photofilename'
            )

class StatSerializer(serializers.ModelSerializer):
    character = serializers.SlugRelatedField(
        queryset=Character.objects.all(),slug_field='character_name'
    )
    class Meta:
        model = Stat
        fields = (
            'character',
            'strength',
            'perception',
            'charisma',
            'speed',
            'intellect'
            )