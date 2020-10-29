from django.shortcuts import render
from django.template import loader
# Real Python
from django.http import HttpResponse
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import status


# Youtube Tutorial w/ angular
from django.views.decorators.csrf import csrf_exempt
from django.http.response import JsonResponse
from django.core.files.storage import default_storage
from rest_framework.parsers import JSONParser 

# import models and serializers
from MobApp.models import Race, Character, Stat
from MobApp.serializers import RaceSerializer, CharacterSerializer, StatSerializer

# Create your views here.
#@csrf_exempt
def index(request):
    races = Race.objects.all()
    template = loader.get_template('MobApp/index.html')
    context = {
        'races_list' :races,
    }
    return HttpResponse(template.render(context,request))


@api_view(['GET','POST'])
def race_collection(request):
    if request.method == 'GET':
        race = Race.objects.all()
        serializer = RaceSerializer(race,many=True)
        return Response(serializer.data)
        #return JsonResponse(serializer.data,safe=False)
    elif request.method == 'POST':
        data = JSONParser().parse(request)
        serializer = RaceSerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data,status=status.HTTP_201_CREATED)
        return Response(serializer.errors,status=status.HTTP_400_BAD_REQUEST)


@api_view(['GET','PUT','DELETE'])
def race_api(request,id):
    try:
        race = Race.objects.get(pk=id)
    except Race.DoesNotExist:
        return HttpResponse(status=404)

    if request.method == 'GET':
        serializer = RaceSerializer(race)
        return Response(serializer.data)

    elif request.method == 'PUT':
        data = JSONParser().parse(request)
        serializer = RaceSerializer(race,data=data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(status=status.HTTP_204_NO_CONTENT)

    elif request.method == 'DELETE':
        race.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)


@api_view(['GET','POST'])
def character_collection(request):
    if request.method == 'GET':
        character = Character.objects.all()
        serializer = CharacterSerializer(character,many=True)
        return Response(serializer.data)

    elif request.method == 'POST':
        data = JSONParser().parse(request)
        serializer = CharacterSerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data,status=status.HTTP_201_CREATED)
        return Response(serializer.errors,status=status.HTTP_400_BAD_REQUEST)


@api_view(['GET','PUT','DELETE'])
def character_api(request,id):
    try:
        character = Character.objects.get(pk=id)
    except Character.DoesNotExist:
        return HttpResponse(status=404)

    if request.method == 'GET':
        serializer = CharacterSerializer(character)
        return Response(serializer.data)

    elif request.method == 'PUT':
        data = JSONParser().parse(request)
        serializer = CharacterSerializer(character,data=data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(status=status.HTTP_204_NO_CONTENT)

    elif request.method == 'DELETE':
        character.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

@api_view(['GET','POST'])
def stat_collection(request):
    if request.method == 'GET':
        stat = Stat.objects.all()
        serializer = StatSerializer(stat,many=True)
        return Response(serializer.data)

    elif request.method == 'POST':
        data = JSONParser().parse(request)
        serializer = StatSerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data,status=status.HTTP_201_CREATED)
        return Response(serializer.errors,status=status.HTTP_400_BAD_REQUEST)


@api_view(['GET','PUT','DELETE'])
def stat_api(request,id):
    try:
        character = Character.objects.get(pk=id)
        stat = Stat.objects.get(character=character)
    except Stat.DoesNotExist:
        return HttpResponse(status=404)

    if request.method == 'GET':
        serializer = StatSerializer(stat)
        return Response(serializer.data)

    elif request.method == 'PUT':
        data = JSONParser().parse(request)
        serializer = StatSerializer(stat,data=data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(status=status.HTTP_204_NO_CONTENT)

    elif request.method == 'DELETE':
        stat.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)


@csrf_exempt
def SaveFile(request):
    image = request.FILES['uploadedFile']
    file_name = default_storage.save(image.name,image)
    return JsonResponse(file_name,safe=False) 