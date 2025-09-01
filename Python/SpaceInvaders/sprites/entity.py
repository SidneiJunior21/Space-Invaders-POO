import pygame

class Entity(pygame.sprite.Sprite):
    def __init__(self):
        super().__init__()
        self.is_alive = True

    def handle_death(self):
        self.is_alive = False
        self.kill()