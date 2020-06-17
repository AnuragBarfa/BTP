from django import forms

from .models import InputDataset
class FileUploadForm(forms.ModelForm):
    # Subject_choices1 = [('', '-------'), ('Bio1', 'Bio1'), ('Cse1', 'Cse1'), ('Math1', 'Math1'), ('Me1', 'Me1')]
    # # description = forms.CharField(max_length=100, required=True)
    # semester=forms.CharField(max_length=50,choices=Subject_choices1,default=' ')
    class Meta:
        model = InputDataset
        fields = ('rankingMethod','fileUse','inputFile' )