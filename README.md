# ComicReader

A simple cbz (no cbr or any other format) reader. 

The only nice capability is to look for a read_order.json file and zoom on single vignette

```
{ "comicbook" : { "pages" : [ 
  {  "img" : "001.jpg",  "from" : { "x" : 0, "y" : 0 },  "to" : { "x" : 1221, "y" : 1644 },  "vignettes": [ 
  ]}
  ,
  {  "img" : "002.jpg",  "from" : { "x" : 0, "y" : 0 },  "to" : { "x" : 1161, "y" : 1606 },  "vignettes": [ 
  ]}
  ,
  {  "img" : "003.jpg",  "from" : { "x" : 0, "y" : 0 },  "to" : { "x" : 1161, "y" : 1606 },  "vignettes": [ 
  ]}
  ,
  {  "img" : "004.jpg",  "from" : { "x" : 0, "y" : 0 },  "to" : { "x" : 1161, "y" : 1606 },  "vignettes": [ 
  ]}
  ,
  {  "img" : "005.jpg",  "from" : { "x" : 0, "y" : 0 },  "to" : { "x" : 1161, "y" : 1606 },  "vignettes": [ 
      { "from" : { "x" : 17, "y": 9},  "to" : { "x" : 580, "y": 537} },
      { "from" : { "x" : 580, "y": 9},  "to" : { "x" : 1145, "y": 537} },
      { "from" : { "x" : 0, "y": 537},  "to" : { "x" : 1162, "y": 1576} }
  ]}
}
```


