from django.db import models
from django.urls import reverse

# Create your models here.
class Race(models.Model):
    race_id = models.AutoField(primary_key=True)
    race_name = models.CharField(max_length=20)
    race_description = models.CharField(max_length=100)

    def __str__(self):
        return self.race_name

class Character(models.Model):
    character_id = models.AutoField(primary_key=True)
    character_key = models.IntegerField(unique=True)
    character_name = models.CharField(max_length=20)
    character_description = models.CharField(max_length=100)
    race = models.ForeignKey(Race,on_delete=models.CASCADE)
    creation_date = models.DateTimeField('Date Created')
    character_photofilename = models.CharField(max_length=100)

    def __str__(self):
        return self.character_name

class Stat(models.Model):
    character = models.ForeignKey('Character',on_delete=models.CASCADE) 
    strength = models.IntegerField(default=0)
    perception = models.IntegerField(default=0)
    charisma = models.IntegerField(default=0)
    speed = models.IntegerField(default=0)
    intellect = models.IntegerField(default=0)
    slug = models.SlugField(null=True,unique=True)

    # def __str__(self):
    #     data = {
    #         'character':self.character,
    #         'strength':strength,
    #         'perception':perception,
    #         'charisma':charisma,
    #         'speed':speed,
    #         'intellect':intellect
    #         }
    #     return printout()
    
    # def get_absolute_url(self):
    #     return reverse('stat_detail',kwargs={'slug':self.slug})

def printout(**kwargs):
    list_vars=[]
    for key in kwargs:
        list_vars.append("{0}\t: {1}\n".format(key,str(kwargs[key])))
    return ''.join(list_vars)

