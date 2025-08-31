import pygame
from .entity import Entity

class Projectile(Entity):
    def __init__(self, x, y, speed, color, screen_height):
        super().__init__()
        self.image = pygame.Surface((5, 15))
        self.image.fill(color)
        self.rect = self.image.get_rect(center=(x, y))
        self.speed = speed
        self.screen_height = screen_height

    def update(self):
        self.rect.y += self.speed
        if self.rect.bottom < 0 or self.rect.top > self.screen_height:
            self.handle_death()